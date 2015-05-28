import akka.actor.{Actor,ActorSystem,Props}
import akka.pattern.ask
import akka.pattern.gracefulStop
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scalaj.http._
import java.lang.Object

class DelayEchoActor extends Actor {
  def receive = {
    case msg: String =>
      import dispatch._, Defaults._
      Thread.sleep(1000)
      sender ! msg
  }
}

class HttpGetRequestActor extends Actor {
  def receive = {
    case url: String =>
      val response: HttpResponse[String] = Http(url).asString
      sender ! response
  }
}

object HelloWorld {
  def main(args: Array[String]) = {
    val echo1Sys = ActorSystem("echo1")
    val echo2Sys = ActorSystem("echo2")
    val http1Sys = ActorSystem("http1")
    val http2Sys = ActorSystem("http2")
    val echo1 = echo1Sys.actorOf(Props[DelayEchoActor])
    val echo2 = echo2Sys.actorOf(Props[DelayEchoActor])
    val http1 = http1Sys.actorOf(Props[HttpGetRequestActor])
    val http2 = http2Sys.actorOf(Props[HttpGetRequestActor])

    implicit val timeout = Timeout(10 seconds)

    val futures = scala.collection.mutable.ArrayBuffer.empty[Future[Any]]
    futures += echo1 ? "あれ？"
    futures += echo2 ? "文字列が"
    futures += echo1 ? "遅れて"
    futures += echo2 ? "流れて"
    futures += echo1 ? "くるよ？"
    futures += http1 ? "http://www.google.co.jp/"
    futures += http2 ? "http://www.google.co.jp/"

    for (future <- futures.toArray) {
      val res = Await.result(future, timeout.duration)
      res match {
        case response: HttpResponse[String] => println(response.body)
        case javaObject: Object => println(javaObject.toString())
      }
    }
  }
}

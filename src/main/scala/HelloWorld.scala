import akka.actor.{Actor,ActorSystem,Props}
import akka.pattern.ask
import akka.pattern.gracefulStop
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

class DelayEchoActor extends Actor {
  def receive = {
    case msg: String =>
      Thread.sleep(1000)
      sender ! msg
  }
}

object HelloWorld {
  def main(args: Array[String]) = {
    var sys = ActorSystem("system")
    val echo = sys.actorOf(Props[DelayEchoActor])

    implicit val timeout = Timeout(10 seconds)

    val futures = scala.collection.mutable.ArrayBuffer.empty[Future[Any]]
    futures += echo ? "あれ？"
    futures += echo ? "文字列が"
    futures += echo ? "遅れて"
    futures += echo ? "流れて"
    futures += echo ? "くるよ？"

    for (future <- futures.toArray) {
      val res = Await.result(future, timeout.duration)
      res match {
        case msg: String => println(msg)
      }
    }

    sys.shutdown()
  }
}

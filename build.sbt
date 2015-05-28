lazy val root = (project in file(".")).
  settings(
    name := "tut",
    version := "1.0",
    scalaVersion := "2.11.6",
    libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4-SNAPSHOT",
    libraryDependencies += "net.databinder.dispatch" % "dispatch-core" % "0.11.2"
    libraryDependencies +=  "org.scalaj" % "scalaj-http" % "1.1.4"
    resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
  )

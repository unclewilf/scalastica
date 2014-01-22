name := "scalastica"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.4" % "test",
  "org.elasticsearch" % "elasticsearch" % "0.90.7",
  "io.argonaut" %% "argonaut" % "6.0.2",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"
)
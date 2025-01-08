name := "MyAkkaProject"

version := "0.1.0"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20",
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",
  "com.typesafe.akka" %% "akka-http" % "10.2.7",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)

name := "MyAkkaProject"

version := "0.1.0"

scalaVersion := "2.13.10"

val akkaVersion = "2.7.0"  // Ensure this is at least 2.7.0
val akkaHttpVersion = "10.5.0" // Use a compatible version

enablePlugins(GatlingPlugin)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.megard" %% "akka-http-cors" % "1.2.0",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "io.gatling" % "gatling-test-framework" % "3.9.5" % Test,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.9.5" % Test
)

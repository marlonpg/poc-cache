package com.gambasoftware

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object Server {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "HttpServer")
    implicit val executionContext = system.executionContext

    val route: Route =
      path("get") {
        get {
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "This is a GET endpoint"))
        }
      } ~
        path("put") {
          put {
            entity(as[String]) { body =>
              complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"Received PUT request with body: $body"))
            }
          }
        }

    val server = Http().newServerAt("localhost", 8080).bind(route)
    server.map { _ =>
      println("Successfully started on http://localhost:8080")
    } recover { case ex =>
      println("Failed to start the server due to: " + ex.getMessage)
    }
  }
}

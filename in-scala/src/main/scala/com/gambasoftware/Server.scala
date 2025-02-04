package com.gambasoftware

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._

final case class CacheEntry(key: String, value: String)

final case class ResponseMessage(message: String)

// Define JSON formatters
trait JsonSupport extends DefaultJsonProtocol {
  implicit val cacheEntryFormat: RootJsonFormat[CacheEntry] = jsonFormat2(CacheEntry)
  implicit val responseMessageFormat: RootJsonFormat[ResponseMessage] = jsonFormat1(ResponseMessage)
}

object Server extends JsonSupport {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "HttpServer")
    implicit val executionContext = system.executionContext

    val route: Route =
      path("cache" / Segment) { key =>
        get {
          CacheServiceV3.get(key) match {
            case Some(value) => complete(CacheEntry(key, value.toString))
            case None => complete(StatusCodes.NotFound, ResponseMessage("Key not found"))
          }
        }
      } ~
        path("cache") {
          put {
            entity(as[CacheEntry]) { entry =>
              CacheServiceV3.put(entry.key, entry.value)
              complete(ResponseMessage(s"Stored key '${entry.key}' with value '${entry.value}'"))
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
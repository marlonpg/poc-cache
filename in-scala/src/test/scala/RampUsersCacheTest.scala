import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class CachePerformanceTest extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080/cache")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val feeder = Iterator.continually(Map(
    "key" -> (scala.util.Random.alphanumeric.take(5).mkString),
    "value" -> (scala.util.Random.alphanumeric.take(10).mkString)
  ))

  val putAndGetScenario = scenario("Cache Load Test")
    .feed(feeder)
    .exec(
      http("Put Cache Entry")
        .put("")
        .body(StringBody("""{ "key": "${key}", "value": "${value}" }""")).asJson
        .check(status.is(200))
    )
    .pause(2)
    .exec(
      http("Get Cache Entry")
        .get("/${key}")
        .check(status.is(200))
        .check(jsonPath("$.value").is("${value}"))
    )

  setUp(
    putAndGetScenario.inject(rampUsers(500) during (10.seconds)),
  ).protocols(httpProtocol)
}

package default

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://192.168.37.207:3000")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:45.0) Gecko/20100101 Firefox/45.0")

	val headers_1 = Map(
		"Accept" -> "text/html, application/xhtml+xml",
		"Turbolinks-Referrer" -> "http://192.168.37.207:3000/people")

	val headers_3 = Map(
		"Accept" -> "text/html, application/xhtml+xml",
		"Turbolinks-Referrer" -> "http://192.168.37.207:3000/people/5")



	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.get("/people"))
		.pause(2)
		.exec(http("request_1")
			.get("/people/new")
			.headers(headers_1))
		.pause(6)
		.exec(http("request_2")
			.post("/people")
			.formParam("utf8", "✓")
			.formParam("authenticity_token", "kypCpGiBhaFPg0HFzX9BljJMiBx1Augs5vk7QDApEqsw+VSfvVxGYamh+PkDaeuThzsEfPnvkLVTrsaRho8s+g==")
			.formParam("person[name]", "Person Name")
			.formParam("commit", "Create Person")
			.resources(http("request_3")
			.get("/people")
			.headers(headers_3)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}

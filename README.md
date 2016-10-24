# example_gatling_maven

Random collection of code snippets for doing helpful things with gatling:

## Find a random link on a page and store as session variable
(findAll then shuffle)
```scala
    check(
      css("a","href")
          .findAll
          .transform(s => util.Random.shuffle(s).head)
          .optional
          .saveAs("randomLink")
    )
```


## Extract either a value OR a blank string into a session variable
```scala
  check(
    css("input[type='text']","name")
          .find
          .transformOption(extract => extract.orElse(Some("")))
          .optional
          .saveAs("formInputName")
  )
```

## Split out check into a method for reuse
This example also splits out the http get into it's own method
```scala
    def extractBaseUrl(): HttpCheck = {
     currentLocation.saveAs("baseUrl")
    }

    def getAndCheck(actionName: String, url : String): ChainBuilder = {
      exec(
        http(actionName)
        .get(url)
        .check(extractBaseUrl)
      )
    }
```

## Define a `val` at the top of a method
We can't use this val within the session, but you can pass it to other methods etc
```scala
    def chain(someLabel: String): ChainBuilder = {

      val actionName = someLabel + "UA-1/2_"
      
      exec(getAndCheck(actionName, "http://${url}/"))
```

## Do something if a session variable is defined/set (and with a random switch
```scala
      exec(http().get("http://nsdasd").check(css("blah").saveAs("formAction")))
      .doIf(session => session("formAction").asOption[Any].isDefined && util.Random.nextInt(10).toInt > 5) {
        exec(some other stuff)
      }
```

## Repeat n times (where n is a random number)
```scala
  repeat(util.Random.nextInt(10).toInt) {
  
  }
```

## Assign a random user agent to different http requests
Also includes a nice default httpConf

###BaseTest.scala:
```scala
package example

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BaseTest extends Simulation {
# Some
# Stuff

  //UA picker
  def randomUA: Expression[String] = {
    s => UAs.userAgents.get(util.Random.nextInt(UAs.userAgents.length))
  }
  
  val httpConf = http
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .inferHtmlResources
    .silentResources
    .disableCaching
    .disableAutoReferer
    .maxConnectionsPerHostLikeChrome
    .maxRedirects(5)
    .userAgentHeader(randomUA)

# Some
# More

  setUp(
    scenario("Frequent User")
      .exec(Example.welcomePageLoad())
      .inject(constantUsersPerSec(0.1) during (1 minutes))
    .protocols(httpConf)
   )
}
```

###UAs.scala:
```scala
package example

object UAs {
  val userAgents = Array(
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36"
  )
}
 

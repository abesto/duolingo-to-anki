package net.abesto.duolingotoanki.scrapers

import dispatch.Defaults._
import dispatch._
import net.abesto.duolingotoanki.Log
import org.asynchttpclient.Response
import org.asynchttpclient.cookie.Cookie
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.collection.JavaConverters._

object DuolingoLogin {

  import net.abesto.duolingotoanki.Constants.Duolingo._
  import net.abesto.duolingotoanki.Utils._

  val loginUrl: Req = url(Login.Request.URL).POST

  def login(username: String, password: String): Either[String, String] = {
    Log.log(s"Trying to log in to Duolingo as $username")
    Http.default(loginUrl.addParameter(Login.Request.Params.USERNAME, username)
      .addParameter(Login.Request.Params.PASSWORD, password)
      .addCommonHeaders()).either.apply() match {
      case Right(res: Response) => parse(res.getResponseBody) match {
        case JObject(List(
        JField(Login.Response.Failure.FAILURE, JString(_)),
        JField(Login.Response.Failure.MESSAGE, JString(message)))
        ) => Log.log(s"Login failed. Duolingo says: $message")
          Left(message)
        case JObject(List(
        JField(Login.Response.Success.RESPONSE, JString(Login.Response.Success.OK)),
        JField(Login.Response.Success.USERNAME, JString(_)),
        JField(Login.Response.Success.USER_ID, JString(userId))
        )) =>
          Log.log(s"Logged in to Duolingo as $username user_id=$userId")
          res.getCookies.asScala.find({ c: Cookie => c.getName == Login.AUTH_HEADER })
            .map(_.getValue)
            .toRight(s"Failed to find expected cookie ${Login.AUTH_HEADER} in response. This is probably a bug in duolingo-to-anki.")
        case other =>
          Log.log(s"Unexpected response for login: ${other.toString}")
          Left("Unexpected response for login. This is a probably a bug in duolingo-to-anki.")
      }
      case Left(ex: Throwable) =>
        Log.log(s"Login error: ${ex.getMessage}")
        Left(ex.getMessage)
    }
  }
}


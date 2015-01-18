import com.ning.http.client.{Cookie, Response}
import dispatch._, Defaults._
import org.json4s._
import org.json4s.native.JsonMethods._
import scala.collection.JavaConverters._

object DuolingoLogin extends Constants {
  val loginUrl = url("https://www.duolingo.com/login").POST

  def login(username: String, password: String): Either[String, String] = {
    Log.log("Trying to log in to Duolingo as " + username)
    Http(loginUrl.addParameter("login", username)
                 .addParameter("password", password)
                 .addHeader("X-Requested-With", "XMLHttpRequest")).either.apply() match {
      case Right(res: Response) => parse(res.getResponseBody) match {
        case JObject(List(
               JField("failure", JString(failure)),
               JField("message", JString(message)))
             ) => Log.log("Login failed. Duolingo says: " + message)
                  Left(message)
        case JObject(List(
        JField("response", JString("OK")),
        JField("username", JString(username2)),
        JField("user_id", JString(userId))
        )) =>
          Log.log(s"Logged in to Duolingo as $username id=$userId")
          Right(res.getCookies.asScala.filter({c:Cookie => c.getName == DUOLINGO_AUTH_HEADER}).head.getValue)
        case other =>
          Log.log("Unexpected response for login: " + other.toString)
          Left("Unexpected response for login. This is a probably a bug in duolingo-to-anki.")
      }
      case Left(ex: Throwable) =>
        Log.log("Login error: " + ex.getMessage)
        Left(ex.getMessage)
    }
  }
}


import com.ning.http.client.{Cookie, Response}
import dispatch._, Defaults._
import org.json4s._
import org.json4s.native.JsonMethods._
import scala.collection.JavaConverters._

object DuolingoLogin extends Constants {
  val loginUrl = url("https://www.duolingo.com/login").POST

  def login(username: String, password: String): Either[String, String] =
    Http(loginUrl.addParameter("login", username).addParameter("password", password)).either.apply() match {
      case Right(res: Response) => parse(res.getResponseBody) match {
        case JObject(List(JField("message", JString(message)))) => Left(message)
        case JObject(List(
        JField("response", JString("OK")),
        JField("username", JString(username2)),
        JField("protocol", JString(protocol))
        )) => Right(res.getCookies.asScala.filter({c:Cookie => c.getName == DUOLINGO_AUTH_HEADER}).head.getValue.toString)
      }
      case Left(ex: Throwable) => Left(ex.getMessage)
    }
}


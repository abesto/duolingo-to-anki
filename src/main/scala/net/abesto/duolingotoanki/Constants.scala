package net.abesto.duolingotoanki
import java.util.ResourceBundle

object Constants {
  val APP_NAME = "Duolingo-to-Anki"
  val APP_VERSION = ResourceBundle.getBundle("version").getString("version")
  val APP_HOMEPAGE = "https://github.com/abesto/duolingo-to-anki"

  val USER_AGENT = s"$APP_NAME $APP_VERSION ($APP_HOMEPAGE)"

  object Duolingo {
    val WWW_DOMAIN = "www.duolingo.com"
    val D2_DOMAIN = "d2.duolingo.com"
    val WWW_BASE_URL = s"https://$WWW_DOMAIN"
    val D2_BASE_URL = s"https://$D2_DOMAIN"

    object Login {

      val AUTH_HEADER = "jwt_token"

      object Request {
        val URL = s"$WWW_BASE_URL/login"

        object Params {
          val USERNAME = "login"
          val PASSWORD = "password"
        }

      }

      object Response {

        object Failure {
          val FAILURE = "failure"
          val MESSAGE = "message"
        }

        object Success {
          val RESPONSE = "response"
          val OK = "OK"
          val USERNAME = "username"
          val USER_ID = "user_id"
        }

      }
    }

    object Vocabulary {

      object Overview {
        val URL = s"$WWW_BASE_URL/vocabulary/overview"
      }

    }

    object Dictionary {

      object Hints {
        val URL = s"$D2_BASE_URL/api/1/dictionary/hints/%s/%s"
        val BATCH_SIZE = 50

        object Params {
          val TOKENS = "tokens"
        }

      }

    }

  }

}

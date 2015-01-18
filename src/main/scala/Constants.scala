object Constants {
  object Duolingo {
    val DOMAIN = "www.duolingo.com"
    val BASE_URL = s"https://$DOMAIN"
    object Login {
      object Request {
        val URL = s"$BASE_URL/login"
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
      val AUTH_HEADER = "auth_tkt"
    }

    object Flashcards {
      val URL = s"$BASE_URL/api/1/flashcards"
      object Params {
        val COUNT = "n"
        val ALLOW_PARTIAL_DECK = "allow_partial_deck"
      }
    }

    object Vocabulary {
      object Overview {
        val URL = s"$BASE_URL/vocabulary/overview"
      }
    }
  }
}

package net.abesto.duolingotoanki.scrapers

import com.ning.http.client.{Cookie, Response}
import dispatch.Defaults._
import dispatch._
import net.abesto.duolingotoanki.{Constants, Log}
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, _}

class DuolingoFlashcardScraper(authToken: String) {
  import net.abesto.duolingotoanki.Constants.Duolingo.Flashcards._
  import net.abesto.duolingotoanki.Utils._

  def fetch(limit: Long = 10000): Either[String, Flashcards] =
    doFetch(limit) match {
      case r@Right(f) =>
        if (f.flashcard_data.length <= limit) {
          Log.log(s"Fetched ${f.flashcard_data.length} flashcard items. Native language: ${f.ui_language}. Learned language: ${f.learning_language}.")
          r
        } else {
          Log.log(s"Fetched ${f.flashcard_data.length} flashcard items, but there seem to be more. Trying again with a higher limit.")
          fetch(limit * 2)
        }
      case l@Left(msg) =>
        Log.log(msg)
        l
    }

  protected def doFetch(limit: Long): Either[String, Flashcards] = {
    val thisUrl = url(URL)
      .addCookie(new Cookie(Constants.Duolingo.WWW_DOMAIN, Constants.Duolingo.Login.AUTH_HEADER, authToken, null, -1, true))
      .addQueryParameter(Params.COUNT, limit.toString)
      .addCommonHeaders()

    implicit val format = DefaultFormats
    Log.log(s"Fetching flashcard items from ${thisUrl.url}")
    Http(thisUrl).either.apply() match {
      case Right(r: Response) =>
        parse(r.getResponseBody)
          .extractOpt[Flashcards]
          .toRight(s"Failed to parse response: ${r.getResponseBody}")
      case Left(ex: Throwable) => Left(s"Failed to fetch flashcard items: $ex")
    }
  }
}

case class Flashcards
( ui_language: String
, learning_language: String
, flashcard_data: Seq[Flashcard]
)

case class Flashcard
( word: String
, translation: String
)



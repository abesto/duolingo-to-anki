import com.ning.http.client.{Response, Cookie}
import dispatch._, Defaults._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.DefaultFormats

class DuolingoVocabularyScraper(authToken: String) {
  import Constants.Duolingo.Vocabulary.Overview._
  import Utils._

  def fetch(): Either[String, Vocabulary] =
    doFetch() match {
      case r@Right(v) =>
          Log.log(s"Fetched ${v.vocab_overview.length} words from vocabulary overview. Native language: ${v.from_language}. Learned language: ${v.learning_language}.")
          r
      case l@Left(msg) =>
        Log.log(msg)
        l
    }

  def doFetch(): Either[String, Vocabulary] = {
    val thisUrl = url(URL)
      .addCookie(new Cookie(Constants.Duolingo.DOMAIN, Constants.Duolingo.Login.AUTH_HEADER, authToken, null, -1, true))
      .addCommonHeaders()

    implicit val format = DefaultFormats
    Log.log(s"Fetching vocabulary overview from ${thisUrl.url}")
    Http(thisUrl).either.apply() match {
      case Right(r: Response) =>
        parse(r.getResponseBody)
          .extractOpt[Vocabulary]
          .toRight(s"Failed to parse response: ${r.getResponseBody}")
      case Left(ex: Throwable) => Left(s"Failed to fetch vocabulary overview: $ex")
    }
  }

}

case class Vocabulary
( from_language: String
, learning_language: String
, vocab_overview: Seq[VocabularyItem])

case class VocabularyItem
( word_string: String
, pos: String
, skill: String
, skill_url_title: String)

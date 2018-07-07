package net.abesto.duolingotoanki.scrapers

import dispatch.Defaults._
import dispatch._
import net.abesto.duolingotoanki.{Constants, Log}
import org.asynchttpclient.Response
import org.asynchttpclient.cookie.Cookie
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, _}

class DuolingoVocabularyScraper(authToken: String) {

  import net.abesto.duolingotoanki.Constants.Duolingo.Vocabulary.Overview._
  import net.abesto.duolingotoanki.Utils._

  def postProcess(vocabulary: Vocabulary): Unit = {
    if (vocabulary.learning_language == "de") {
      vocabulary.vocab_overview.foreach { vi =>
        if (vi.pos == "Noun") {
          vi.word_string = vi.word_string.capitalize
        }
      }
    }
  }

  def fetch(): Either[String, Vocabulary] =
    doFetch() match {
      case r@Right(v) =>
        postProcess(v)
        Log.log(s"Fetched ${v.vocab_overview.length} words from vocabulary overview. Native language: ${v.from_language}. Learned language: ${v.learning_language}.")
        r
      case l@Left(msg) =>
        Log.log(msg)
        l
    }

  def doFetch(): Either[String, Vocabulary] = {
    val thisUrl = url(URL)
      .addCookie(new Cookie(
        Constants.Duolingo.Login.AUTH_HEADER,
        authToken,
        false,
        Constants.Duolingo.WWW_DOMAIN,
        null,
        -1,
        true,
        false)
      ).addCommonHeaders()

    implicit val format = DefaultFormats
    Log.log(s"Fetching vocabulary overview from ${thisUrl.url}")
    Http.default(thisUrl).either.apply() match {
      case Right(r: Response) =>
        parse(r.getResponseBody)
          .extractOpt[Vocabulary]
          .toRight(s"Failed to parse response: ${r.getResponseBody}")
      case Left(ex: Throwable) => Left(s"Failed to fetch vocabulary overview: $ex")
    }
  }

}

case class Vocabulary
(from_language: String
 , learning_language: String
 , vocab_overview: Seq[VocabularyItem])

case class VocabularyItem
(var word_string: String
 , pos: String
 , skill: String
 , skill_url_title: String)

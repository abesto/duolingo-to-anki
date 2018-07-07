package net.abesto.duolingotoanki.scrapers

import dispatch.Defaults._
import dispatch._
import net.abesto.duolingotoanki.Constants.Duolingo.Dictionary.Hints._
import net.abesto.duolingotoanki.Log
import org.asynchttpclient.Response
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, _}


class DuolingoDictionaryHintsScraper() {

  import net.abesto.duolingotoanki.Utils._

  def fetch(vocabulary: Vocabulary): Either[String, Map[String, Seq[String]]] =
    vocabulary.vocab_overview.map(_.word_string).grouped(BATCH_SIZE)
      .map(doFetch(vocabulary.from_language, vocabulary.learning_language, _))
      .reduce((a, b) => a.right.map(_ ++ b.right.getOrElse(Map())))

  protected def doFetch(fromLang: String, toLang: String, words: Seq[String]): Either[String, Map[String, Seq[String]]] = {
    val quotedWords = words.map('"' + _ + "\"")
    val thisUrl = url(URL.format(toLang, fromLang))
      .addQueryParameter(Params.TOKENS, s"[${quotedWords.mkString(",")}]")
      .addCommonHeaders()

    implicit val format: Formats = DefaultFormats
    Log.log(s"Fetching word hints for ${words.length} words (${words.mkString(", ")}) from ${thisUrl.url}")
    Http.default(thisUrl).either.apply() match {
      case Right(r: Response) =>
        parse(r.getResponseBody)
          .extractOpt[Map[String, Seq[String]]]
          .toRight(s"Failed to parse response: ${r.getResponseBody}")
      case Left(ex: Throwable) => Left(s"Failed to fetch words hints items: $ex")
    }
  }
}

import dispatch._, Defaults._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

case class DuolingoTranslationScraper(foreign: String, native: String) {
  private def scrapeTranslations(words: Seq[Word]) {
    Console.println("# Fetching translations for words: " + words.map({_.foreign}).mkString(","))
    val myUrl = url("https://d.duolingo.com/words/hints/" + foreign + "/" + native)
        .addQueryParameter("tokens", compact(render(words.map({_.foreign}))))
    val req = Http(myUrl OK as.String)
    implicit var formats = DefaultFormats
    val hints = parse(req())
    Console.println("# " + compact(render(hints)))
    for (word <- words) {
      word.translations = (hints \ word.foreign).extract[List[String]]
    }
  }

  def scrapeTranslationsPaged(words: Seq[Word]) {
    words.grouped(10).foreach(scrapeTranslations)
  }
}

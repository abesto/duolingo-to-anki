import com.ning.http.client.Cookie
import dispatch._, Defaults._
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.JsonMethods._


case class WordList(language: String, words: Seq[Word]) {}


case class DuolingoWordlistScraper(authToken: String) extends Constants {
  val wordsDomain = "www.duolingo.com"

  def fetchPage(page: Int) = Http(url("https://" + wordsDomain + "/words?page=" + page.toString).addCookie(new Cookie(
    wordsDomain, DUOLINGO_AUTH_HEADER, authToken, null, -1, true
  )) OK as.String).apply()

  def scrapeWordsPage(page: Int) = {
    parseToWords(fetchPage(page))
  }

  def parseToWords(s: String) : Seq[Word] =  {
    implicit val formats = Serialization.formats(NoTypeHints)
    val json = parse(s)
    for {
      JObject(word) <- json \ "vocab"
      JField("language", JString(language)) <- word
      JField("forms_data", JArray(forms_datas)) <- word
      forms_data <- forms_datas
    } yield new Word(
      language,
      (forms_data \ "surface_form").extract[String],
      Seq(),
      (forms_data \ "skill" \ "short").extract[String]
    )
  }

  def fetchAllWords(lang: String): WordList = {
    var page = 1
    var words: Seq[Word] = Seq()
    var wordsThisPage: Seq[Word] = null
    do {
      wordsThisPage = null
      Log.log("Scraping page " + page)
      wordsThisPage = scrapeWordsPage(page)
      Log.log(wordsThisPage.map{_.foreign}.mkString(","))
      words ++= wordsThisPage
      page += 1
    } while (wordsThisPage.length > 0)
    implicit val formats = Serialization.formats(NoTypeHints)
    val lang = (parse(fetchPage(1)) \ "language").extract[String]
    new WordList(
      lang,
      words.distinct.filter{_.language == lang}
    )
  }
}

import java.io.{PrintWriter, File}

class AnkiExporter(f: File) {
  def quote(sep: String)(s: Seq[String]) = '"' + s.mkString(sep) + '"'

  def write(words: Seq[Word]): Either[String, Unit] = {
    try {
      val pw = new PrintWriter(f, "UTF-8")
      try {
        pw.write("#### Start of scraping log\n")
        for (logline <- Log.lines) pw.write("# " + logline + "\n")
        pw.write("#### End of scraping log, data coming up\n")
        for (word <- words) {
          pw.write(
            Seq(word.learned, word.native)
              .map(quote("\n"))
              .:+(quote(" ")(word.tags))
              .mkString(";")
              .++("\n\n")
          )
        }
        Right()
      } finally {
        pw.close()
      }
    } catch {
      case e: Throwable => Left(e.toString)
    }
  }
}

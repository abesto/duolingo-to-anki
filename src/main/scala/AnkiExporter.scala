import java.io.{PrintWriter, File}

class AnkiExporter(f: File) {
  def write(words: Seq[Word]) {
    val pw = new PrintWriter(f, "UTF-8")
    try {
      pw.write("#### Start of scraping log\n")
      for (logline <- Log.lines) pw.write("# " + logline + "\n")
      pw.write("#### End of scraping log, data coming up\n")
      for (word <- words) {
        pw.write(
          "\"" + word.foreign + "\";\"" + word.translations.mkString("\n") + "\";\"" + word.skill + "\"\n\n"
        )
      }
    } finally {
      pw.close()
    }
  }
}

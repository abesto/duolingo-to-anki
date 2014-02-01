class AnkiExporter {
  def write(words: Seq[Word]) {
    for (word <- words) {
      Console.println(
        "\"" + word.foreign + "\";\"" + word.translations.mkString("\n") + "\";\"" + word.skill + "\"\n"
      )
    }
  }
}

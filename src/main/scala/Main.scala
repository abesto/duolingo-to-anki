object Main extends App {
  DuolingoLogin.login(readLine("Username: "),readLine("Password: ")) match {
    case Left(err) => println("Login failed. Duolingo says: " + err)
    case Right(authToken) =>
      println("Login successful. Auth token: " + authToken)
      val words = new DuolingoWordlistScraper(authToken).fetchAllWords("fr")
      Console.println("# Full word list: " + words.map{_.foreign}.mkString(","))
      new DuolingoTranslationScraper("fr", "en").scrapeTranslationsPaged(words)
      new AnkiExporter().write(words)
  }
}

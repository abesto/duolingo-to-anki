object Translators {
  def duolingoToAnki(flashcards: Flashcards, vocabulary: Vocabulary): Either[String, Seq[Word]] = {
    if (flashcards.learning_language != vocabulary.learning_language) {
      Left(s"Flashcards learned language ${flashcards.learning_language} doesn't match vocabulary learned language ${vocabulary.learning_language}")
    } else {
      Right(
        flashcards.flashcard_data.map(f => {
          val vocabItems = vocabulary.vocab_overview.filter(v => f.learning_words.contains(v.word_string))
          val tags = {
            if (vocabItems.length == 0) {
              Log.log(s"No vocabulary item found for flashcard (${f.learning_words.mkString(", ")}). No tags will be added.")
              Seq()
            } else {
              vocabItems.flatMap(vi => Seq(vi.pos, vi.skill_url_title))
            }
          }
          new Word(f.learning_words, f.ui_words, tags)
        })
      )
    }
  }
}

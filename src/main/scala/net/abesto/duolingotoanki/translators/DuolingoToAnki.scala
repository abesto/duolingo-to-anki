package net.abesto.duolingotoanki.translators

import net.abesto.duolingotoanki.scrapers.{Flashcards, Vocabulary}
import net.abesto.duolingotoanki.{Log, Word}

object DuolingoToAnki {
  def translate(flashcards: Flashcards, vocabulary: Vocabulary): Either[String, Seq[Word]] = {
    if (flashcards.learning_language != vocabulary.learning_language) {
      Left(s"Flashcards learned language ${flashcards.learning_language} doesn't match vocabulary learned language ${vocabulary.learning_language}")
    } else {
      Right(
        flashcards.flashcard_data.map(f => {
          val vocabItems = vocabulary.vocab_overview.filter(v => f.word == v.word_string)
          val tags = {
            if (vocabItems.length == 0) {
              Log.log(s"No vocabulary item found for flashcard (${f.word}). No tags will be added.")
              Seq()
            } else {
              vocabItems.flatMap(vi => Seq(vi.pos, vi.skill_url_title))
            }
          }
          new Word(Seq(f.word), Seq(f.translation), tags)
        })
      )
    }
  }
}

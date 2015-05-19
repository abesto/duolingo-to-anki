package net.abesto.duolingotoanki.translators

import net.abesto.duolingotoanki.scrapers.Vocabulary
import net.abesto.duolingotoanki.{Log, Word}

object DuolingoToAnki {
  def translate(vocabulary: Vocabulary, hints: Map[String, Seq[String]]): Either[String, Seq[Word]] = {
    Right(
      vocabulary.vocab_overview.flatMap(vi => {
        hints.get(vi.word_string) match {
          case None =>
            Log.log(s"No translations found for ${vi.word_string}")
            Seq()
          case Some(translations) => Seq(new Word(Seq(vi.word_string), translations, Seq(vi.skill, vi.pos)))
        }
      })
    )
  }
}

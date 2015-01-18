duolingo-to-anki
================

Download word list from [Duolingo](http://duolingo.com), save into [Anki](http://ankisrs.net/)-compatible format. Duolingo is an online language learning tool; Anki is a flash-card application useful for practicing your vocabulary.

If you only want to do flashcard reviews of the words you learn on Duolingo, you'll probably have an easier time just using the flashcard system built right into Duolingo. To reach it, click on "Words" in the top menu, then "Review flashcards" on the right.

## Releases

You can download the latest version from the [Releases](https://github.com/abesto/duolingo-to-anki/releases/) page right here on GitHub.

## Security
You'll be asked for your username and password each time; they are not saved and are transmitted over HTTPS securely. This can probably be improved, see [Issue #6](https://github.com/abesto/duolingo-to-anki/issues/6).

## Usage
 * On Duolingo, select the language you want to use as the foreign language. (Limitation: currently English is always the "native" language in `duolingo-to-anki`.)
 * Enter your Duolingo username and password
 * Click Go!
 * Watch (or go for a coffee) as your words are downloaded
 * Click OK on the dialog telling you how many words were downloaded, and in which language
 * In the newly opened save dialog save to a text file you'll find in the next step
 * Import the text file you've saved as described in [the Anki manual](http://ankisrs.net/docs/manual.html#importing). 
   * The first field is the word in the foreign language
   * The second field is the list of translations in the native language (always English for now)
   * The third field is the "skill" in which the word appears on Duolingo
   
Have fun!

## Building from source

In case you want to hack on `duolingo-to-anki`. Pull requests are welcome.

The only prerequisite is [SBT](http://www.scala-sbt.org/).

```sh
git clone https://github.com/abesto/duolingo-to-anki.git
cd duolingo-to-anki
sbt run
```

To build a distributable fat jar:

```sh
sbt one-jar
```

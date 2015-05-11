duolingo-to-anki
================

Download word list from [Duolingo](http://duolingo.com), save into [Anki](http://ankisrs.net/)-compatible format. Duolingo is an online language learning tool; Anki is a flash-card application useful for practicing your vocabulary.

If you only want to do flashcard reviews of the words you learn on Duolingo, you'll probably have an easier time just using the flashcard system built right into Duolingo. To reach it, click on "Words" in the top menu, then "Review flashcards" on the right.

## Releases

You can download the latest version from the [Releases](https://github.com/abesto/duolingo-to-anki/releases/) page right here on GitHub.

Tested with
 * Anki version 2.0.31
 * Duolingo on 2015-01-18
 * OSX Yosemite and Windows 7 (not that the platform should matter)

## Usage
 * On Duolingo, select the language you want to use as the foreign language.
 * Enter your Duolingo username and password
 * Click Go!
 * Watch as your words are downloaded
 * In the newly opened save dialog save to a text file you'll find in the next step
 * Import the text file you've saved as described in [the Anki manual](http://ankisrs.net/docs/manual.html#importing). 
   * The first field is the word in the foreign language
   * The second field is the list of translations in the native language (always English for now)
   * The third field contains the  "skill" in which the word appears on Duolingo, and the part of speech (noun, verb, etc.) as tags
   
Have fun!

## Reporting issues

If you encounter any problem at all using `duolingo-to-anki`, please feel free to [open an issue](https://github.com/abesto/duolingo-to-anki/issues/new) right here on GitHub. You may first want to check whether your issue is already being worked on by goint to the [Issues](https://github.com/abesto/duolingo-to-anki/issues) page. Feature requests, ideas are also welcome!

## Security
You'll be asked for your username and password each time; they are not saved and are transmitted over HTTPS securely. This can probably be improved, see [Issue #6](https://github.com/abesto/duolingo-to-anki/issues/6).

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

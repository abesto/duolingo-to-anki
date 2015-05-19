package net.abesto.duolingotoanki

import java.awt.Font
import java.io.File
import javax.swing.UIManager

import net.abesto.duolingotoanki.exporters.AnkiExporter
import net.abesto.duolingotoanki.scrapers.{DuolingoDictionaryHintsScraper, DuolingoLogin, DuolingoVocabularyScraper}
import net.abesto.duolingotoanki.translators.DuolingoToAnki

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.swing.GridBagPanel._
import scala.swing.{Font, _}

object Main extends SimpleSwingApplication {
  Log.register(new Log.Handler {
    override def handleClear(): Unit = {}

    override def handleLine(line: String): Unit = println(line)
  })

  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

  def top = new MainFrame {
    title = Constants.APP_NAME
    preferredSize = new Dimension(800, 400)
    contents = {
      new GridBagPanel {
        val c = new Constraints
        c.fill = Fill.Both

        val log = new TextArea()
        val logScroll = new ScrollPane()
        val usernameLabel = new Label()
        val username = new TextField()
        val passwordLabel = new Label()
        val password = new PasswordField()

        Log.register(new Log.Handler {
          override def handleClear() {}

          override def handleLine(line: String) {
            log.append(line + "\n")
            logScroll.verticalScrollBar.value = logScroll.verticalScrollBar.maximum
          }
        })

        def error(err: Any) {
          Log.log(err.toString)
          Dialog.showMessage(parent = contents.head, message = err, title = "Error :(", messageType = Dialog.Message.Error)
        }


        def words(): Either[String, Seq[Word]] = for {
          authToken <- DuolingoLogin.login(username.text, password.password.mkString).right
          vocabulary <- new DuolingoVocabularyScraper(authToken).fetch().right
          translations <- new DuolingoDictionaryHintsScraper().fetch(vocabulary).right
          words <- DuolingoToAnki.translate(vocabulary, translations).right
        } yield words

        def areYouSureYouWantToOverwrite(file: File): Boolean =
          Dialog.showConfirmation(
            contents.head,
            '"' + file.getAbsolutePath + "\" already exists. Do you want to replace it?",
            "A file or folder with the same name already exists. Replacing it will overwrite its current contents.",
            Dialog.Options.YesNo,
            Dialog.Message.Warning
          ) == Dialog.Result.Yes

        def write(ws: Seq[Word]): Unit = Swing.onEDT {
          val fileChooser = new FileChooser()
          if (fileChooser.showSaveDialog(contents.head) != FileChooser.Result.Approve) {
            Log.log("Save dialog cancelled")
          }
          val file = fileChooser.selectedFile

          if (file.exists() && !areYouSureYouWantToOverwrite(file)) {
            return write(ws)
          }

          val result = new AnkiExporter(file).write(ws)
          result.right.map(_ => Log.log("Successfully wrote " + file.getAbsolutePath))
          result.left.map(s => {
            error(s"Failed to write $file: $s")
            write(ws)
          })
        }

        val goAction = new Action("Go!") {
          override def apply() {
            Future {
              Log.clear()
              val ws = words()
              ws.left.map(error)
              ws.right.map(write)
            }
          }
        }

        log.editable = false
        log.font = new Font(Font.MONOSPACED, 0, 12)
        log.wordWrap = true

        c.gridx = 0
        c.gridy = 0
        c.gridwidth = 2
        c.weightx = 1
        c.weighty = 0.9
        logScroll.preferredSize = new Dimension(700, 350)
        logScroll.contents = log
        layout(logScroll) = c

        c.gridwidth = 1
        c.weighty = 0.05

        usernameLabel.text = "Username"
        c.gridx = 0
        c.gridy = 1
        layout(usernameLabel) = c

        username.minimumSize = new Dimension(150, 30)
        username.action = goAction
        c.gridx = 1
        layout(username) = c

        passwordLabel.text = "Password"
        c.gridy = 2
        c.gridx = 0
        layout(passwordLabel) = c

        password.minimumSize = new Dimension(150, 30)
        password.action = goAction
        c.gridx = 1
        layout(password) = c

        val goButton = new Button()
        goButton.action = goAction
        c.gridy = 3
        c.gridx = 0
        c.gridwidth = 2
        layout(goButton) = c
      }
    }

    Log.log(s"${Constants.USER_AGENT} initialized")
  }
}

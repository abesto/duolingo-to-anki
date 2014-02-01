import java.awt.Font
import javax.swing.JFileChooser
import scala.swing._
import scala.swing.Font
import scala.swing.GridBagPanel._
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits._

object Main extends SimpleSwingApplication {
  Log.register(new Log.Handler {
    override def handleClear(): Unit = {}
    override def handleLine(line: String): Unit = println(line)
  })

  def top = new MainFrame {
    title = "Duolingo to Anki"
    preferredSize = new Dimension(700, 400)
    contents = { new GridBagPanel {
      val c = new Constraints
      c.fill = Fill.Both

      val log = new TextArea()
      val logScroll = new ScrollPane()
      val usernameLabel = new Label()
      val username = new TextField()
      val passwordLabel = new Label()
      val password = new PasswordField()

      Log.register(new Log.Handler {
        override def handleClear(): Unit = log.text = ""
        override def handleLine(line: String) {
          log.append(line + "\n")
          logScroll.verticalScrollBar.value = logScroll.verticalScrollBar.maximum
        }
      })

      val goAction = new Action("Go!") {
        override def apply() {
          future {
            Log.clear()
            DuolingoLogin.login(username.text, password.password.mkString) match {
              case Left(err) => Dialog.showMessage(message=err, title="Login failed")
              case Right(authToken) =>
                val wordList = new DuolingoWordlistScraper(authToken).fetchAllWords("fr")
                val native = "en"  // TODO: make this an input
                new DuolingoTranslationScraper(wordList.language, native).scrapeTranslationsPaged(wordList.words)
                Dialog.showMessage(message="Downloaded " + wordList.words.size + " words in " + wordList.language, title="Success!")
                val fileChooser = new JFileChooser()
                if (fileChooser.showSaveDialog(top.peer) == JFileChooser.APPROVE_OPTION) {
                  new AnkiExporter(fileChooser.getSelectedFile).write(wordList.words)
                }
            }
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
    }}
  }
}

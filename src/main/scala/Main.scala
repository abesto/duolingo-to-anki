import javax.swing.JFileChooser
import scala.swing._
import scala.swing.GridBagPanel._

object Main extends SimpleSwingApplication {
  Log.register(new Log.Handler {
    override def handleClear(): Unit = {}
    override def handleLine(line: String): Unit = println(line)
  })

  def top = new MainFrame {
    title = "Duolingo to Anki"
    preferredSize = new Dimension(400, 250)
    contents = { new GridBagPanel {
      val c = new Constraints
      c.fill = Fill.Both

      val goAction = new Action("Go!") {
        override def apply() {
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

      val usernameLabel = new Label()
      usernameLabel.text = "Username"
      c.gridx = 0
      c.gridy = 0
      layout(usernameLabel) = c

      val username = new TextField()
      username.preferredSize = new Dimension(150, 30)
      username.action = goAction
      c.gridx = 1
      layout(username) = c

      val passwordLabel = new Label()
      passwordLabel.text = "Password"
      c.gridy = 1
      c.gridx = 0
      layout(passwordLabel) = c

      val password = new PasswordField()
      password.preferredSize = new Dimension(150, 30)
      password.action = goAction
      c.gridx = 1
      layout(password) = c

      val goButton = new Button()
      goButton.action = goAction
      c.gridy = 2
      c.gridx = 0
      c.gridwidth = 2
      layout(goButton) = c
    }}
  }
}

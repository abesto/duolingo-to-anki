import java.text.SimpleDateFormat
import java.util.Date
import javax.swing.SwingUtilities


object Log {
  trait Handler {
    def handleLine(line: String): Unit
    def handleClear(): Unit
  }

  var handlers: Set[Handler] = Set()
  def register(handler: Handler) = handlers += handler

  var lines: Seq[String] = Seq()
  val dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.S]")
  def log(line: String) = {
    val timestampedLine = dateFormat.format(new Date) + " " + line
    lines ++= Seq(timestampedLine)
    handlers foreach {h => h.handleLine(timestampedLine)}
  }

  def clear() {
    lines = Seq()
    SwingUtilities.invokeLater(new Runnable {
      override def run() = handlers foreach {h => h.handleClear()}
    })
  }
}

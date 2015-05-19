package net.abesto.duolingotoanki

import java.text.SimpleDateFormat
import java.util.Date
import javax.swing.SwingUtilities


object Log {

  val dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]")
  var handlers: Set[Handler] = Set()
  var lines: Seq[String] = Seq()

  def register(handler: Handler) = handlers += handler

  def log(line: String) = {
    val timestampedLine = dateFormat.format(new Date) + " " + line
    lines ++= Seq(timestampedLine)
    handlers foreach { h => h.handleLine(timestampedLine) }
  }

  def clear() {
    lines = Seq()
    SwingUtilities.invokeLater(new Runnable {
      override def run() = handlers foreach { h => h.handleClear() }
    })
  }

  trait Handler {
    def handleLine(line: String): Unit

    def handleClear(): Unit
  }
}

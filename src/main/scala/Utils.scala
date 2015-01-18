import dispatch.Req
import scala.language.implicitConversions

object Utils {
  val name = "Duolingo-to-Anki"
  val version = "0.2.1"
  val homepage = "https://github.com/abesto/duolingo-to-anki"

  val userAgent = s"$name $version ($homepage)"

  class ReqExtensions(r: Req) {
    def addCommonHeaders(): Req =
      r
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("User-Agent", userAgent)
  }

  implicit def reqExtensions(r: Req): ReqExtensions = new ReqExtensions(r)
}

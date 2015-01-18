package net.abesto.duolingotoanki

import dispatch.Req
import scala.language.implicitConversions

object Utils {
  class ReqExtensions(r: Req) {
    def addCommonHeaders(): Req =
      r
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("User-Agent", Constants.USER_AGENT)
  }

  implicit def reqExtensions(r: Req): ReqExtensions = new ReqExtensions(r)
}

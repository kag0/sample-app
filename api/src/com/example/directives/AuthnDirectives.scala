package com.example.directives

import akka.http.scaladsl.server.Directive1
import com.example.auth.UserAuthContext

trait AuthnDirectives {
  def requireUserAuthn: Directive1[UserAuthContext] = ???
}

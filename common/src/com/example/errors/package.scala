package com.example

import com.example.util.AutoSomeMagnet
import io.circe.{Encoder, Json}

package object errors {

  trait Error {
    def error: String
    def message: Option[String]
  }

  object Error {
    implicit val encoder = new Encoder[Error] {
      def apply(a: Error) = {
        // I don't like this at all, there must be a better way
        val js = Json.obj("error" -> Json.fromString(a.error))
        a.message match {
          case Some(m) =>
            js.deepMerge(Json.obj("message" -> Json.fromString(m)))
          case None => js
        }
      }
    }
  }

  case class ResourceNotFound(
      resource: String = "Resource",
      id: AutoSomeMagnet[String] = None
  ) extends Error {
    val error = s"${resource}NotFound"
    val message = Some(s"$resource ${id.value match {
      case Some(i) => s": $i "
      case None    => ""
    }} not found.")
  }

}

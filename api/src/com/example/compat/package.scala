package com.example

import akka.http.scaladsl.marshalling.Marshaller
import scala.concurrent.Future
import monix.eval.Task
import monix.execution.Scheduler
import akka.http.scaladsl.server.PathMatcher1
import java.util.UUID
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server.PathMatcher.Matched
import akka.http.scaladsl.server.PathMatcher.Unmatched
import scala.util.Try

package object compat {
  implicit def taskToFutureMarshaller[A, B](implicit
      futureAMarshaller: Marshaller[Future[A], B],
      scheduler: Scheduler
  ): Marshaller[Task[A], B] = futureAMarshaller.compose[Task[A]](_.runToFuture)

  object Id extends PathMatcher1[UUID] {
    def apply(path: Path) =
      path match {
        case Path.Segment(segment, tail) =>
          Try(UUID.fromString(segment))
            .fold(
              __ => Unmatched,
              id => Matched(tail, Tuple1(id))
            )
        case _ => Unmatched
      }
  }
}

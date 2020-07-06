package com.example.routing

import java.util.UUID

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.server.Directives._
import com.example.authz.NoteAuthz
import com.example.compat._
import com.example.directives.AuthnDirectives
import com.example.payloads.{CreateNoteRequest, _}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import monix.execution.Scheduler
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.NoContent
import com.example.errors.ResourceNotFound

class NoteRoutes(authz: NoteAuthz)(implicit scheduler: Scheduler)
    extends FailFastCirceSupport
    with AuthnDirectives {

  val routes =
    concat(
      path("notes")(
        concat(
          get(listNotes),
          post(createNote)
        )
      ),
      path("notes" / Id)(id =>
        concat(
          get(retrieveNote(id)),
          delete(deleteNote(id))
        )
      )
    )

  def retrieveNote(id: UUID) =
    requireUserAuthn(implicit auth =>
      onSuccess(authz.retrieve(id).runToFuture) {
        case Some(note) => complete(NoteResponse(note))
        case None       => complete(ResourceNotFound("Note", id.toString))
      }
    )

  lazy val listNotes =
    requireUserAuthn(implicit auth =>
      parameters("page".as[Int] ? 1, "limit".as[Int] ? 10)((page, limit) =>
        complete(
          for {
            total <- authz.count
            notes <- authz.list(page, limit)
          } yield ListNotesResponse(
            total,
            Uri("/notes")
              .withQuery(
                Query(
                  "page"  -> page.toString,
                  "limit" -> limit.toString
                )
              ),
            Uri("/notes")
              .withQuery(
                Query(
                  "page"  -> (page + 1).toString,
                  "limit" -> limit.toString
                )
              ),
            notes.map(NoteResponse(_))
          )
        )
      )
    )

  lazy val createNote =
    requireUserAuthn(implicit auth =>
      entity(as[CreateNoteRequest])(request =>
        complete(
          authz.create(request.title, request.body).map(NoteResponse(_))
        )
      )
    )

  def deleteNote(id: UUID) =
    requireUserAuthn(implicit auth =>
      onSuccess(authz.deleteNote(id).runToFuture) {
        case Some(_) => complete(NoContent)
        case None    => complete(ResourceNotFound("Note", id.toString))
      }
    )
}

package com.example.authz

import com.example.services.NoteContract
import com.example.auth.UserAuthContext
import java.util.UUID
import monix.eval.Task

class NoteAuthz(noteService: NoteContract) {
  def count(implicit auth: UserAuthContext) = noteService.count(auth.userId)

  def retrieve(id: UUID)(implicit auth: UserAuthContext) =
    noteService.retrieve(id).map(_.filter(auth.userId == _.userId))

  def list(page: Int, limit: Int)(implicit auth: UserAuthContext) =
    noteService.listByUser(auth.userId, page, limit)

  def create(title: String, body: String)(implicit auth: UserAuthContext) =
    noteService.create(auth.userId, title, body)

  def deleteNote(id: UUID)(implicit auth: UserAuthContext) =
    noteService
      .showOwner(id)
      .flatMap {
        case Some(auth.userId) => noteService.delete(id).map(Some(_))
        case _                 => Task.pure(None)
      }
}

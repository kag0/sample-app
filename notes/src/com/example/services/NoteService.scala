package com.example.services

import com.example.models.Note
import java.util.UUID
import monix.eval.Task
import java.time.Clock
import io.getquill.PostgresMonixJdbcContext
import io.getquill.SnakeCase
import com.example.util.Db._
import java.time.OffsetDateTime

class NoteService(ctx: PostgresMonixJdbcContext[SnakeCase])(implicit
    clock: Clock
) extends NoteContract {

  import ctx._

  private val nonDeletedQuery = quote(query[Note].filter(_.deletedAt.isEmpty))

  def retrieve(id: UUID) =
    ctx
      .run(nonDeletedQuery.filter(_.id == lift(id)))
      .map(_.headOption)

  def create(userId: UUID, title: String, body: String) = {
    val note = Note(userId, title, body)

    ctx
      .run(
        query[Note]
          .insert(lift(note))
          .returningGenerated(n => (n.id, n.createdAt))
      )
      .map { case (id, ca) => note.copy(id = id, createdAt = ca) }
  }

  def delete(id: UUID) =
    ctx
      .run(
        nonDeletedQuery
          .filter(_.id == lift(id))
          .update(_.deletedAt -> lift(Option(OffsetDateTime.now(clock))))
      )
      .map(_ => ())

  def showOwner(id: UUID) =
    ctx
      .run(query[Note].filter(_.id == lift(id)).map(_.userId))
      .map(_.headOption)

  def count(userId: UUID) = ctx.run(listByUserQuery(userId).size)

  def listByUser(userId: UUID, page: Int, limit: Int) =
    ctx.run(listByUserQuery(userId).drop(lift(page * limit)).take(lift(limit)))

  private def listByUserQuery(userId: UUID) =
    quote(nonDeletedQuery.filter(lift(userId) == _.userId))
}

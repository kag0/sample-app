package com.example.payloads

import java.time.OffsetDateTime
import java.util.UUID
import com.example.models.Note

case class NoteResponse(
    id: UUID,
    title: Option[String],
    body: String,
    createdAt: OffsetDateTime
)

object NoteResponse {
  def apply(note: Note) =
    new NoteResponse(note.id, note.title, note.body, note.createdAt)
}

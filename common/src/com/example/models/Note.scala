package com.example.models

import java.util.UUID
import java.time.OffsetDateTime
import java.time.Instant
import java.time.ZoneOffset

case class Note(
    id: UUID,
    userId: UUID,
    title: String,
    body: String,
    createdAt: OffsetDateTime,
    deletedAt: Option[OffsetDateTime]
)

object Note {
  def apply(userId: UUID, title: String, body: String) =
    new Note(
      new UUID(0, 0),
      userId,
      title,
      body,
      OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC),
      None
    )
}

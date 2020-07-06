package com.example.util

import io.getquill.MappedEncoding
import java.util.UUID
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Date

object Db {
  implicit val encodeUUID = MappedEncoding[UUID, String](_.toString)
  implicit val decodeUUID = MappedEncoding[String, UUID](UUID.fromString(_))

  implicit val encodeOffsetDateTime =
    MappedEncoding[OffsetDateTime, Date](z => Date.from(z.toInstant))
  implicit val decodeOffsetDateTime =
    MappedEncoding[Date, OffsetDateTime](d =>
      d.toInstant.atOffset(ZoneOffset.UTC)
    )

}

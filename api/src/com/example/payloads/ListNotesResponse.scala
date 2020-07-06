package com.example.payloads

import akka.http.scaladsl.model.Uri

case class ListNotesResponse(
    total: Long,
    _links: ListNotesResponseLinks,
    _embedded: ListNotesResponseEmbeds
)

object ListNotesResponse {
  def apply(
      total: Long,
      href: Uri,
      next: Uri,
      notes: Seq[NoteResponse]
  ): ListNotesResponse =
    ListNotesResponse(
      total,
      ListNotesResponseLinks(ListNotesResponseLink(href, next)),
      ListNotesResponseEmbeds(notes)
    )

}

case class ListNotesResponseLinks(self: ListNotesResponseLink)

case class ListNotesResponseLink(href: Uri, next: Uri)

case class ListNotesResponseEmbeds(notes: Seq[NoteResponse])

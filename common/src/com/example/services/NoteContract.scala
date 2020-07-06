package com.example.services

import java.util.UUID
import monix.eval.Task

import com.example.models.Note

trait NoteContract {
  def retrieve(id: UUID): Task[Option[Note]]
  def listByUser(userId: UUID, page: Int, limit: Int): Task[Seq[Note]]
  def create(userId: UUID, title: String, body: String): Task[Note]
  def delete(id: UUID): Task[Unit]
  def showOwner(id: UUID): Task[Option[UUID]]
  def count(userId: UUID): Task[Long]
}

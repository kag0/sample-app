package com.example.services

import java.util.UUID

import monix.eval.Task
import com.example.models.Note
import com.example.util.Patch

trait NoteContract {
  def retrieve(id: UUID): Task[Option[Note]]
  def listByUser(userId: UUID, page: Int, limit: Int): Task[Seq[Note]]
  def create(userId: UUID, title: String, body: String): Task[Note]
  def update(id: UUID, title: Patch[String], body: Option[String]): Task[Unit]
  def delete(id: UUID): Task[Unit]
  def showOwner(id: UUID): Task[Option[UUID]]
  def count(userId: UUID): Task[Long]
}

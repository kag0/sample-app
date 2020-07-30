package com.example.util

import cats.Applicative

sealed trait Patch[+A] {
  def handle[F[_]: Applicative](
      update: A => F[Unit],
      clear: => F[Unit]
  ): F[Unit]
}

object Patch {
  implicit class OptionSyntax[A](val maybe: Option[A]) extends AnyVal {
    def handle[F[_]: Applicative](update: A => F[Unit]) =
      maybe.toPatch.handle(update, Applicative[F].unit)

    def toPatch = maybe.fold[Patch[A]](Nop)(Update(_))
  }
}

case class Update[A](value: A) extends Patch[A] {
  def handle[F[_]: Applicative](
      update: A => F[Unit],
      clear: => F[Unit]
  ) = update(value)
}

case object Clear extends Patch[Nothing] {
  def handle[F[_]: Applicative](
      update: Nothing => F[Unit],
      clear: => F[Unit]
  ) = clear
}

case object Nop extends Patch[Nothing] {
  def handle[F[_]: Applicative](
      update: Nothing => F[Unit],
      clear: => F[Unit]
  ) = Applicative[F].unit
}

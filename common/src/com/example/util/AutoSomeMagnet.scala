package com.example.util

class AutoSomeMagnet[+A](val value: Option[A]) extends AnyVal {
  override def toString = s"Auto$value"
}

object AutoSomeMagnet {
  implicit def fromValue[A](a: A): AutoSomeMagnet[A] =
    new AutoSomeMagnet(Some(a))
  implicit def fromOption[A](maybe: Option[A]): AutoSomeMagnet[A] =
    new AutoSomeMagnet(maybe)
  implicit def unauto[A](mag: AutoSomeMagnet[A]): Option[A] = mag.value
}

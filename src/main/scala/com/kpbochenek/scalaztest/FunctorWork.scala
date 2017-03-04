package com.kpbochenek.scalaztest

import scalaz.Functor

/** Created by kpbochenek on 11/15/16. */

object FunctorWork {

  def main(args: Array[String]): Unit = {

    implicit val fListOps: Functor[List] = new Functor[List] {
      override def map[A, B](fa: List[A])(f: (A) => B): List[B] = fa.map(f)
    }

    implicit val fOptionOps: Functor[Option] = new Functor[Option] {
      override def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = fa.map(f)
    }

    println(fListOps.map(List(1, 2, 3))(x => x+3))
    println(fOptionOps.map(Some(2))(x => x+3))

    val fCompOps = fListOps.compose(fOptionOps)

    println(fCompOps.map(List(Some(1), None, Some(3)))(x => x+6))
  }
}

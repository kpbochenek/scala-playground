package com.kpbochenek

import scalaz.{Failure, Semigroup, Success, Validation}

/** Created by kpbochenek on 8/3/16. */

object ProgrammingScala4 {

  def check(str: String): Validation[List[String], Int] = {
    if (str.contains(" ")) { Failure(List(s"$str - no space allowed")) }
    else if (str.contains(".")) { Failure(List(s"$str - no DOT allowed")) }
    else Success(str.length)
  }

  def main(args: Array[String]): Unit = {
    println("P4")

    def genListSemigroup[T](): Semigroup[List[T]] = new Semigroup[List[T]] {
      override def append(f1: List[T], f2: => List[T]): List[T] = f1 ++ f2
    }

    implicit val slist: Semigroup[List[String]] = genListSemigroup[String]()

    implicit val sint: Semigroup[Int] = new Semigroup[Int] {
      override def append(x: Int, y: => Int): Int = x + y
    }

    import scalaz.Semigroup._

    val r = check("ala") +++ check("tomek") +++ check("geez error") +++ check("okpo") +++ check("???.???")

    println(r)

  }
}

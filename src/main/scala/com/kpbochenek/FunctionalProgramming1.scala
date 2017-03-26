package com.kpbochenek

import scala.collection.immutable.Stream.cons


object FunctionalProgramming1 {

  def main(args: Array[String]): Unit = {
    println("LESSON 1")

    val opt: Option[Int] = Some(4)

    opt.flatMap(x => Some(x))

    val optAgeS: Option[Int] = Some(2)
    val optAgeN: Option[Int] = None
    val optTickets: Option[Int] = Some(3)

    map2(optAgeS, optTickets)((a, b) => println(a, b))
    map2(optAgeN, optTickets)((a, b) => println(a, b))

    println(sequence(List(Some(1), Some(2), None, Some(4))))
    println(sequence(List(Some(1), Some(2), Some(3), Some(4))))

    println(traverse(List(1, 2, 3, 4, 5))(i => if (i == 4) None else Some(i+10)))
    println(traverse(List(1, 2, 3, 4, 5))(i => if (i == 14) None else Some(i+100)))

    val x = cons(1, Stream(3))
    println(x)
    println("STREAM:")
    x.foreach(println)

  }

  def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a.flatMap(av => b.map(bv => f(av, bv)))
  }

  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    a.foldRight(Option(List[A]()))((a2opt, a1) => a1.flatMap(old => a2opt.map(a2 => a2 :: old)))
  }

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    a.foldRight(Option(List[B]()))((a2, a1) => a1.flatMap(old => f(a2).map(r => r :: old)))
  }

  def seq2[A](a: List[Option[A]]): Option[List[A]] = {
    traverse(a)(x => x)
  }
}

package com.kpbochenek

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scalaz.Applicative


class TestedClass {
  def computeHeavyStuff(a: Int, ev1: Int => Int, ev2: Int => Int): Int = {
    a + ev1(a) + ev2(a)
  }

//  def computeHeavyStuff(a: Int, conv: Int => Future[Int]): Future[Int] = {
//
//  }

  def simplyAdd(a: Int, b: Int): Int = a + b

  def runInContext[F[_] : Applicative, A, B, C](a: F[A], b: F[B], f: (A,B) => C): F[C] = {
    val ev = implicitly[Applicative[F]]
    ev.apply2[A, B, C](a, b)(f)
  }
}

object ProgrammingScala5 {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scalaz._
  import std.scalaFuture._

  def main(args: Array[String]): Unit = {
    val fa = Future.successful(2)
    val fb = Future.successful(3)

//    val tc = new TestedClass()
//
//    val result = Apply[Future].apply2(fa, ???)(tc.computeHeavyStuff)
//
//    println(Await.result(result, 1.second))
  m2(args)
  }


  def m2(args: Array[String]): Unit = {
    val tc = new TestedClass()
    val a = 2
    val b = Future.successful(3)

    val result: Future[Int] = Apply[Future].apply2(Future.successful(a), b)(tc.simplyAdd)

    println(Await.result(result, 1.second))

    val r2 = tc.runInContext(Future.successful(a), b, tc.simplyAdd)
    println(Await.result(r2, 1.second))
  }
}

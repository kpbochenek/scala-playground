package com.kpbochenek

/** Created by kpbochenek on 11/11/16. */

object ProgrammingScala6 {

  trait ExampleTrait1 {
    type T1
    type T2 <: T1
  }

  class Service {
    class Logger {
      def log(message: String): Unit = println(s"log: $message")
    }
    val logger: Logger = new Logger
  }

  val s1 = new Service
//  val s2 = new Service { override val logger = s1.logger } // ERROR logger should be of type this.Logger

  def main(args: Array[String]): Unit = {
    println("Scala6")


  }

  // typeclass which allows us to add two values of the same type
  trait ADD[T] {
    def add(a: T, b: T): T
  }

  object Add {

    implicit def numericLike[T : Numeric]: ADD[T] = new ADD[T] {
      override def add(a: T, b: T): T = implicitly[Numeric[T]].plus(a, b)
    }

    // implementation for Int
//    implicit val addInt= new ADD[Int] {
//      def add(i1: Int, i2: Int): Int = i1 + i2
//    }

    // implementation for pair of integers
    implicit val addIntIntPair = new ADD[(Int,Int)] {
      def add(p1: (Int,Int), p2: (Int,Int)): (Int,Int) =
        (p1._1 + p2._1, p1._2 + p2._2)
    }

    implicit val addString = new ADD[String] {
      override def add(a: String, b: String): String = a + " " + b
    }
  }

  // typeclass which allows us to reduce some container of values to single value
  trait REDUCE[T, -M[T]] {
    def reduc(col: M[T])(f: (T,T) => T): T
  }

  object Reduce {
    implicit def seqReduce[T] = new REDUCE[T, Seq] {
      override def reduc(col: Seq[T])(f: (T, T) => T): T = col.reduce(f)
    }

    implicit def optionReduce[T] = new REDUCE[T, Option] {
      override def reduc(col: Option[T])(f: (T, T) => T): T = col.reduce(f)
    }
  }

  trait REDUCE1[-M[_]] {
    def reduc[T](col: M[T])(f: (T,T) => T): T
  }

  object Reduce1 {
    implicit def seqReduce1 = new REDUCE1[Seq] {
      override def reduc[T](col: Seq[T])(f: (T, T) => T): T = col.reduce(f)
    }

    implicit def optionReduce1 = new REDUCE1[Option] {
      override def reduc[T](col: Option[T])(f: (T, T) => T): T = col.reduce(f)
    }
  }

  object Utils {
    // function which allows us to add values in any container
//    def sumAny[T : ADD, M[T]](container: M[T])(implicit red: REDUCE[T,M]): T =
//      red.reduc(container)(implicitly[ADD[T]].add(_,_))

    // or another implementation
    def sumAny[T : ADD, M[_] : REDUCE1](container: M[T]): T =
      implicitly[REDUCE1[M]].reduc(container)(implicitly[ADD[T]].add(_,_))
  }

  import Add._
  import Reduce1._
//  println(sum(Vector(1, 2, 3, (x: Int,y: Int) => x+y)))
  println(Utils.sumAny(Vector(1, 2, 3, 4)))
  println(Utils.sumAny(Vector((1, 1), (2, 1), (3, 1), (4, 1))))
  println(Utils.sumAny(List(3, 4, 5)))
  println(Utils.sumAny(List("ala", "ma", "kota")))
  println(Utils.sumAny(List(1.0, 2.2, 5.1)))

  println(Utils.sumAny(Some(3)))
//  println(Utils.sumAny(None.asInstanceOf[Option[Int]])) << unsupported operation exception


  trait Functor[A,+M[_]] {
    def map2[B](f: A => B): M[B]
  }

  object Functor {

    implicit class SeqFunctor[A](seq: Seq[A]) extends Functor[A,Seq] {
      def map2[B](f: A => B): Seq[B] = seq map f
    }

    implicit class OptionFunctor[A](opt: Option[A]) extends Functor[A,Option] {
      def map2[B](f: A => B): Option[B] = opt map f
    }

//    implicit class MapValueFunctor[K,V1](mapKV1: Map[K,V1]) extends Functor[V1,({type λ[α] = Map[K,α]})#λ] {
//      def map2[V2](f: V1 => V2): Map[K,V2] = mapKV1 map {
//        case (k,v) => (k,f(v))
//      }
//    }

    implicit class MapKeyFunctor[K1,V](mapKV1: Map[K1,V]) extends Functor[K1,({type λ[α] = Map[α,V]})#λ] {
//    implicit class MapKeyFunctor[K1,V](mapKV1: Map[K1,V]) extends Functor[K1, λ[α => Map[α,V]]] {
      def map2[K2](f: K1 => K2): Map[K2,V] = mapKV1 map {
        case (k,v) => (f(k),v)
      }
    }
  }

  import Functor._
  println(Vector(1,2,3).map2(_.toString))

  println(Map(1 -> "a", 2 -> "b", 3 -> "c").map2(_ / 2))
  println("DONE")
}

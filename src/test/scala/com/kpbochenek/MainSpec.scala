package com.kpbochenek

import java.time.Instant

import cats.data.{Kleisli, Reader, State}
import cats.free.Free
import org.specs2.mutable.Specification

import scalaz.{-\/, \/, \/-}

class MainSpec extends Specification {

  "Main" should {
    "add 2 numbers" in {


      type Stack = List[Int]

      val pop = State[Stack, Int] {
        case x :: xs => (xs, x)
      }

      def push(a: Int) = State[Stack, Unit] {
        case xs => (a :: xs, ())
      }

      def add3ToHead: State[Stack, Int] = for {
        _ <- push(3)
        a <- pop
        b <- pop
      } yield a + b

      def add1ToHead: State[Stack, Int] = for {
        _ <- push(1)
        a <- pop
        b <- pop
      } yield a + b

      val add3And1ToHead2: State[Stack, Int] = for {
        a <- add3ToHead
        b <- add1ToHead
      } yield a+b

      val t3: State[Stack, Int] = for {
        _ <- State.pure(3)
        _ <- State.modify[Stack](2 :: _)
        _ <- State.pure(11)
      } yield 4

      println("!!!!!!!!!!!!!1")
      println(add3ToHead.run(List(1,2,3,4)).value)
      println(add1ToHead.run(List(11,12,13,14)).value)
      println("!!!!!!!!!!!!!1")
      println(add3And1ToHead2.run(List(1, 2, 3, 4, 5)).value)
      println("!!!!!!!!!!!!!1")

      (2 + 4) should beEqualTo(6)
    }

    "Kleisli" in {
      val add1: Kleisli[Option, Int, Int] = Kleisli[Option, Int, Int](x => Some(x + 1))
      println(add1 run 3)

      val ra: Reader[Int, Int] = Reader((x: Int) => x * 2)
      val rb = Reader((x: Int) => x + 10)

      val read = ra.flatMap(a => rb.map(b => a + b + 5))
//      val read: Reader[Int, Int] = for {
//        a <- ra(3)
//        b <- rb
//      } a + b

      println(read.run(3))

      (2 + 4) should beEqualTo(6)
    }

  }
}
/*
case class Account(no: String, name: String, idNo: String, opened: Instant, closed: Option[Instant])

trait IdVerifier {
  def verifyId(idNo: String, name: String): Boolean
}

trait AccountService {
  type Error = String
  type ErrorOr[A] = Error \/ A

  def open(no: String, name: String, idNo: String, opened: Instant): IdVerifier => ErrorOr[Account] =
    { (v: IdVerifier) => if (v.verifyId(idNo, name)) \/-(Account(no, name, idNo, opened, None)) else -\/("INVALID") }

}

object Elements {

  sealed trait AccountRepoF[+A]

  case class Query(no: String) extends AccountRepoF[Account]

  case class Store(account: Account) extends AccountRepoF[Unit]

  case class Delete(no: String) extends AccountRepoF[Unit]

  type AccountRepo[A] = Free[AccountRepoF, A]
}
*/
package com.kpbochenek.cats

import cats.data.{Coproduct, State}
import cats.free.{Free, Inject}
import cats.{Id, ~>}
import com.kpbochenek.cats.FenwickPureInterpreter.FenwickTreeState
import com.kpbochenek.cats.FenwickTreeModel.FenwickTreeModel

import scala.com.kpbochenek.cats.FenwickTree


object FenwickTreeModel {

  sealed trait FenwickTreeModel[A]
  case class Put(pos: Int, count: Int) extends FenwickTreeModel[Unit]
  case class Scale(value: Int) extends FenwickTreeModel[Unit]
  case class GetSingle(pos: Int) extends FenwickTreeModel[Int]
  case class GetSum(pos: Int) extends FenwickTreeModel[Int]

  class FenwickActions[F[_]](implicit I: Inject[FenwickTreeModel, F]) {
    def put(pos: Int, count: Int): Free[F, Unit] =
      Free.inject[FenwickTreeModel, F](Put(pos, count))
    def scale(value: Int): Free[F, Unit] =
      Free.inject[FenwickTreeModel, F](Scale(value))
    def getSingle(pos: Int): Free[F, Int] =
      Free.inject[FenwickTreeModel, F](GetSingle(pos))
    def getSum(pos: Int): Free[F, Int] =
      Free.inject[FenwickTreeModel, F](GetSum(pos))
  }

  object FenwickActions {
    implicit def interacts[F[_]](implicit I: Inject[FenwickTreeModel, F]): FenwickActions[F] = new FenwickActions[F]
  }
}


class FenwickImpureInterpreter(size: Int) extends (FenwickTreeModel ~> Id) {
  import FenwickTreeModel._

  var ft: FenwickTree = new FenwickTree(size)

  override def apply[A](fa: FenwickTreeModel[A]): Id[A] = fa match {
    case Put(pos, count) =>
      println(s"put($pos, $count)")
      ft.add(pos, count)
      ()
    case Scale(value) =>
      println(s"scale($value)")
      ft.scale(value)
      ()
    case GetSingle(pos) =>
      println(s"getSingle($pos)")
      ft.get(pos).asInstanceOf[A]
    case GetSum(pos) =>
      println(s"getSum($pos)")
      ft.sum(pos).asInstanceOf[A]
  }
}




object FenwickPureInterpreter {
  type FenwickTreeState[A] = State[FenwickTree, A]


  val pureInterpreter = new (FenwickTreeModel ~> FenwickTreeState) {
    import FenwickTreeModel._

    override def apply[A](fa: FenwickTreeModel[A]): FenwickTreeState[A] = fa match {
      case Put(pos, count) =>
        println(s"put($pos, $count)")
        State.modify(x => {x.add(pos, count); x})
      case Scale(value) =>
        println(s"scale($value)")
        State.modify(x => {x.scale(value); x})
      case GetSingle(pos) =>
        State.inspect(_.get(pos).asInstanceOf[A])
      case GetSum(pos) =>
        println(s"getSum($pos)")
        State.inspect(_.sum(pos).asInstanceOf[A])
    }
  }
}

object UserInteractions {

  sealed trait UserInteraction[A]

  case class AskNumber(test: String) extends UserInteraction[Int]

  case class DisplayUser(text: String, value: Int) extends UserInteraction[Unit]

  class Interacts[F[_]](implicit I: Inject[UserInteraction, F]) {

    def askNumber(text: String): Free[F, Int] =
      Free.inject[UserInteraction, F](AskNumber(text))
    def displayUser(text: String, value: Int): Free[F, Unit] =
      Free.inject[UserInteraction, F](DisplayUser(text, value))

  }

  object Interacts {
    implicit def interacts[F[_]](implicit I: Inject[UserInteraction, F]): Interacts[F] = new Interacts[F]
  }
}

object UserInterpreter extends (UserInteractions.UserInteraction ~> Id) {
  import UserInteractions._

  override def apply[A](fa: UserInteraction[A]): Id[A] = fa match {
    case AskNumber(text) =>
      println(text)
      val v = Console.readInt()
      println("Received: " + v)
      v
    case DisplayUser(text, value) =>
      println("DISPLAY !@!@ " + text + value)
      ()
  }
}

object UserInterpreterPure extends (UserInteractions.UserInteraction ~> FenwickTreeState) {
  import UserInteractions._

  override def apply[A](fa: UserInteraction[A]): FenwickTreeState[A] = fa match {
    case AskNumber(text) =>
      println(text)
      val v = Console.readInt()
      println("Received: " + v)
      State.pure(v.asInstanceOf[A])
    case DisplayUser(text, value) =>
      println("DISPLAY !@!@ " + text + value)
      State.pure(().asInstanceOf[A])
  }
}


object FenwickExample {
  import FenwickTreeModel._
  import UserInteractions._

  type FullApp[A] = Coproduct[FenwickTreeModel, UserInteraction, A]


  def program(implicit I : Interacts[FullApp],
              D : FenwickActions[FullApp]): Free[FullApp, Int] = {

    import D._

    for {
      _ <- put(3, 1)
      _ <- put(7, 5)
      _ <- put(4, 2)
      v <- getSum(5)
    } yield v
  }

  def program2(implicit I : Interacts[FullApp],
              D : FenwickActions[FullApp]): Free[FullApp, Int] = {

    import I._

    for {
      a <- askNumber("Provide number A")
      b <- askNumber("Provide number B")
      _ <- displayUser("Your sum is", a + b)
    } yield a + b
  }

  def program3(implicit I : Interacts[FullApp],
               D : FenwickActions[FullApp]): Free[FullApp, Int] = {

    import I._, D._

    for {
      a <- askNumber("Provide pa")
      _ <- put(a, 1)
      b <- askNumber("Provide pb")
      _ <- put(b, 1)
      c <- askNumber("Provide pc")
      _ <- put(c, 1)
      q <- askNumber("what number to sum to?")
      result <- getSum(q)
      _ <- displayUser("Your sum is", result)
    } yield result
  }

  def main(args: Array[String]): Unit = {
    println("RUN")

    val ftImpreter = new FenwickImpureInterpreter(20)

    val interpreterImpure: FullApp ~> Id = ftImpreter or UserInterpreter

    // impure version
    val result1: Int = program.foldMap(interpreterImpure)
    println(result1)

    // pure version
//    val result2 = program.foldMap(FenwickPureInterpreter.pureInterpreter).run(new FenwickTree(20)).value
//    println(result2)

    // asking you to provide 2 numbers
//    program2.foldMap(interpreter)

//    program3.foldMap(interpreterImpure)

    val interpreterPure: FullApp ~> FenwickTreeState =
      FenwickPureInterpreter.pureInterpreter or UserInterpreterPure

    val result = program3.foldMap(interpreterPure).run(new FenwickTree(20)).value

    println("RESULT!! ")
    println(result)
  }
}


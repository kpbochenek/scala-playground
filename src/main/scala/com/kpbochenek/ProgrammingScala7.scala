package com.kpbochenek

import scalaz.{@@, Tag}

/** Created by kpbochenek on 1/6/17. */

object ProgrammingScala7 {

  sealed trait CustomerId
  sealed trait OrderId
  sealed trait WorkerId

  case class Id(id: Int)

  case class IdParam[T](value: IdTag[T])
  type IdTag[T] = Id @@ T

  def makeId[T](id: Id): Id @@ T = Tag[Id, T](id)


  sealed trait KiloGram
  def KiloGram[A](a: A): A @@ KiloGram = Tag[A, KiloGram](a)


  def main(args: Array[String]): Unit = {

    val mass = KiloGram(20.0)

    println(mass)
    println(2 * Tag.unwrap(mass))

    val sid: Set[IdParam[CustomerId]] = Set(IdParam(makeId[CustomerId](Id(1))), IdParam(makeId[CustomerId](Id(2))), IdParam(makeId[CustomerId](Id(3))))

    println(sid)

    def toIds[T](idParams: Set[IdParam[T]]): Set[IdTag[T]] = idParams.map(_.value)

    println(toIds(sid))

    def toIds2[S <: Iterable[IdParam[T]], T](idParams: S): Iterable[IdTag[T]] = idParams.map(_.value)

    println(toIds2[Set[IdParam[CustomerId]], CustomerId](sid))

    def toIds3[I <: Iterable[IdParam[T]], T](idParams: I): Iterable[IdTag[T]] = idParams.map(_.value)

    println(toIds3[Set[IdParam[CustomerId]], CustomerId](sid))

  }
}

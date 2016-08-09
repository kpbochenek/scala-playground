package com.kpbochenek

import scala.collection.mutable

/** Created by kpbochenek on 7/31/16. */

object ProgrammingScala3 {

  // wrapped class
  class Meters(val value: Int) extends AnyVal

  // wrapper
  class RichMeters(val value: Int) extends AnyVal {
    def add3: Int = value + 3
  }

  object RichMeters {
    implicit def wrap(m: Meters): RichMeters = new RichMeters(m.value)
  }

  def main(args: Array[String]): Unit = {
    println("MAAAAA")

    import RichMeters._

    println(new Meters(3).add3)

    val arr: Array[Int] = Array(3, 1, 2)

    val len = arr.length

    val plus1: mutable.ArrayOps[Int] = arr.map(_ + 1)
    val plus2: mutable.WrappedArray[Int] = arr.map(_ + 2)

    println(s"plus1 ${plus1}")
    println(s"plus2 ${plus2}")


    println(s"3 === ${arr}")
  }
}

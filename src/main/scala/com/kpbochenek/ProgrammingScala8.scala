package com.kpbochenek

import akka.util.TypedMultiMap

import scala.collection.mutable

/** Created by kpbochenek on 1/14/17. */

object ProgrammingScala8 {

  case class Element(x: Int, y: Int)

  def main(args: Array[String]): Unit = {
    implicit val elementOrdering: Ordering[Element] = new Ordering[Element] {
      override def compare(x: Element, y: Element): Int = {
        if (x == y) return 0
        if (x.x > y.x || (x.x == y.x && x.y > y.y)) 1 else -1
      }
    }
    val q = mutable.PriorityQueue[Element]()

    q += Element(1, 1)
    q += Element(1, 3)
    q += Element(1, 5)

    q += Element(3, 2)
    q += Element(4, 2)
    q += Element(4, 4)

    println(q)

    while (q.nonEmpty) {
      val el = q.dequeue()
      println(el)
    }

    val a = mutable.SortedSet()
    a.add(Element(1, 1))
    a.add(Element(1, 2))

    println(a)
  }
}
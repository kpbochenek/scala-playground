package com.kpbochenek

import scalaz.stream.Process


object Main {
  def main(args: Array[String]): Unit = {
    println("MAIN")

    runTest()
  }

  def process(p: Process[Nothing, Int]) {
    val result = p.map(_ + 10).filter(_ % 2 == 0).map(_.toString())
    println(result)
    println(result.toSource.runLog.run)

  }

  private def runTest() {
    val p = Process(1, 2, 3, 4, 5)
    process(p)
  }

  def add2Numbers(a: Int, b: Int) = a + b
}

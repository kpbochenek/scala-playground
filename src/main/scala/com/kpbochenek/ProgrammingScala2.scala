package com.kpbochenek

import com.{AbstractId, CatId, DogId}

/** Created by kpbochenek on 7/31/16. */



object ProgrammingScala2 {
  def main(args: Array[String]): Unit = {
    println("START")

    val cat = CatId.generate()

    println(s"CAT: ${cat} ---> ${cat.id}")

    println(s"GOOOOO ${CatId.generate()}")
    println(s"GOOOOO ${DogId.generate()}")

    val abs: AbstractId = DogId.generate()
    println(s"ABS: $abs")
  }
}

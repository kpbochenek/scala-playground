package com.kpbochenek


object FunctionalProgramming2 {

  def main(args: Array[String]): Unit = {
    println("LESSON 2")

    val rng = SimpleRNG(12345)
    println(rng.nextInt)
    println(rng.nextInt)
    println(rng.nextInt._2.nextInt)

    println("-------")
    println(runner())
    println(runner()(SimpleRNG(123)))
  }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    rng.nextInt match { case (i, rngg) => (Math.abs(i), rngg)}
  }

  def double(rng: RNG): (Double, RNG) = {
    nonNegativeInt(rng) match { case (i, rngg) => (i / Integer.MAX_VALUE, rngg)}
  }


  type Rand[+A] = RNG => (A, RNG)

  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] = {
      r => {
        val (a, rg) = f(r)
        g(a)(rg)
      }
  }

  implicit class RandMonad[T](f: Rand[T]) {
    def flatMap[B](g: T => Rand[B]): Rand[B] = {
      r => {
        val (a, rg) = f(r)
        g(a)(rg)
      }
    }

    def map[B](g: T => B): Rand[B] = {
      r => {
        val (a, rg) = f(r)
        (g(a), rg)
      }
    }
  }

  val rint: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def runner(): Rand[List[Int]] = {
    val ns: Rand[List[Int]] = for {
      x <- rint
      y <- rint
      z <- rint
    } yield List(x, y, z)
    ns
  }
}

trait RNG {
  def nextInt: (Int, RNG)
}

case class SimpleRNG(seed: Long) extends RNG {
  override def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >> 16).toInt
    (n, nextRNG)
  }
}
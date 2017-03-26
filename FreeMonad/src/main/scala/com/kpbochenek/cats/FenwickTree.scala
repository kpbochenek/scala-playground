package scala.com.kpbochenek.cats


class FenwickTree(val size: Int) {
  import FenwickTree._

  val a: Array[Int] = new Array[Int](size)

  // sum of elements from 0 to position
  def sum(position: Int): Int = {
    var sum = 0
    var i = position
    while (i > 0) {
      sum += a(i)
      i -= LSB(i)
    }
    sum
  }

  def add(position: Int, count: Int = 1): Unit = {
    var i = position
    while (i < size) {
      a.update(i, a(i) + count)
      i += LSB(i)
    }
  }

  def scale(c: Int): Unit = {
    var i = 1
    while (i < size) {
      a.update(i, a(i) * c)
      i += 1
    }
  }

  def range(left: Int, right: Int): Int = {
    var sum = 0
    var i = left
    var j = right
    while (j > i) {
      sum += a(j)
      j -= LSB(j)
    }
    while (i > j) {
      sum -= a(i)
      i -= LSB(i)
    }
    sum
  }

  def get(position: Int): Int = range(position-1, position)
}


object FenwickTree {
  def LSB(i: Int): Int = i & (-i)
}
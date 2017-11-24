package com.kpbochenek.scalaz

import scalaz._

class ScalazExample {

  def applyExample(p1: Option[Int], p2: Option[Int]): Option[Int] = {
    import std.option._

    Apply[Option].apply2(p1, p2)((a, b) => a+b)
  }

  def flattening(a: Option[Option[Int]]): Option[Int] = {
    import scalaz.std.option.optionInstance
    import optionInstance.monadSyntax._

    a.join
  }
}

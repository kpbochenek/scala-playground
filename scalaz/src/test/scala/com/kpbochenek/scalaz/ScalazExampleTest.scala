package com.kpbochenek.scalaz

import org.scalatest.FunSuite

class ScalazExampleTest extends FunSuite {

  val underTest = new ScalazExample()

  test("Apply should work for 2 values") {
    assert(underTest.applyExample(Some(1), Some(2)).contains(3))
  }

  test("Apply should do nothing when only one value") {
    assert(underTest.applyExample(Some(1), None).isEmpty)
  }

  test("Flattening returns element for existing element") {
    assert(underTest.flattening(Some(Some(5))).contains(5))
  }

  test("Flattening returns nothing when no element") {
    assert(underTest.flattening(Some(None)).isEmpty)
  }

}

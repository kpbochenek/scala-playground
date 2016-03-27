package com.kpbochenek

import org.specs2.mutable.Specification

class MainSpec extends Specification {

  "Main" should {
    "add 2 numbers" in {
      Main.add2Numbers(2, 4) should beEqualTo(6)
    }
  }
}

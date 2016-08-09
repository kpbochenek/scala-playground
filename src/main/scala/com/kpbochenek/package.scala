package com

import java.util.UUID

/** Created by kpbochenek on 7/31/16. */


sealed abstract class AbstractId(val id: String)
final case class CatId(override val id: String) extends AbstractId(id)
final case class DogId(override val id: String) extends AbstractId(id)

package object kpbochenek {


  class GeneratorLike[TC <: AbstractId](x: TC) {
    def generate(): TC = x
  }

  abstract class PrefixLike[TC <: AbstractId] {
    val prefix: String
  }

  implicit object CatIdPrefix extends PrefixLike[CatId] {
    val prefix: String = "CAT"
  }

  implicit object DogIdPrefix extends PrefixLike[DogId] {
    val prefix: String = "DOG"
  }

  implicit def IdFn[TC <: AbstractId](to: {def apply(s: String): TC})(implicit ev: PrefixLike[TC]): GeneratorLike[TC] =
    new GeneratorLike[TC](to.apply(ev.prefix + "-" + UUID.randomUUID()))


  // --------------------------------------------
  // version with companion object


  abstract class GenericId(val id: String)
  final case class ChairId(override val id: String) extends GenericId(id)
  final case class TableId(override val id: String) extends GenericId(id)


  trait GeneratorStuffLike[TC <: GenericId] { this: Singleton =>
    val prefix: String
    def apply(id: String): TC
    def generate(): TC = apply(prefix + "-" + UUID.randomUUID())
  }

  object ChairId extends GeneratorStuffLike[ChairId] {
    val prefix = "CHAIR"
  }


  // ---------------------------------------------
  // version with working AnyVal

  sealed trait AnimalId extends Any { def id: String }
  final case class FishId(id: String) extends AnyVal with AnimalId
  final case class TurtleId(id: String) extends AnyVal with AnimalId

  val a: AnimalId = FishId("3")
  println(s"${a.id}")
}

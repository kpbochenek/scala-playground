package com.kpbochenek

import java.time.Instant

/** Created by kpbochenek on 8/9/16. */

case class Tweet(id: Int, text: String, time: Double)




abstract class EnumBase {
  def code: String
}

abstract class EnumObject[EnumT <: EnumBase] {}

sealed abstract case class StateCode(code: String) extends EnumBase

object StateCode extends EnumObject[StateCode] {}

case class ClassWithStateCode(id: Int, x: StateCode)



object StackoverflowAnswersJson4s {

  def main(args: Array[String]): Unit = {
    println("STACKOVERFLOW")

    import org.json4s._
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    import org.json4s.native.Serialization
    import org.json4s.native.Serialization.{read, write}

    implicit val formats = DefaultFormats + FieldSerializer[Tweet]()

    val json = List(1, 2, 3)

    println(write(json))

//    print(compact(render(Tweet(1, "ala ma kota", Instant.now()))))


    val json2 = parse("""{"id": 1, "text": "ala ma kota", "time": 12345.3}""")

    val tweet = json2.extract[Tweet]
    println(tweet)
    println(write(tweet))
    println(read[Tweet](write(tweet)))




    //---------------------------------------------


    class EnumSerializer[EnumT <: EnumBase : scala.reflect.ClassTag](enumObject: EnumObject[EnumT])
      extends org.json4s.Serializer[EnumObject[EnumT]] {

      val EnumerationClass = scala.reflect.classTag[EnumT].runtimeClass

      def deserialize(implicit format: Formats):
      PartialFunction[(TypeInfo, JValue), EnumObject[EnumT]] = {
        case (TypeInfo(EnumerationClass, _), jsonx) => jsonx match {
          case i: JString => enumObject
        }
      }

      def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
        case i: EnumT => JString(i.code)
        case _ => JString("BLAH")
      }
    }


//    println(write(ClassWithStateCode(2, StateCode)))

  }
}

object StackoverflowAnswers {
  def main(args: Array[String]): Unit = {
    println("STACKOVERFLOW")
  }
}
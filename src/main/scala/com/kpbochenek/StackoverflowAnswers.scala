package com.kpbochenek

import java.io.{ByteArrayOutputStream, PrintWriter}
import java.util
import java.util.concurrent._

import com.typesafe.config.{Config, ConfigFactory}
import org.json4s.ext.EnumSerializer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.ClassTag

/** Created by kpbochenek on 8/9/16. */

case class Tweet(id: Int, text: String, time: Double)




abstract class EnumBase {
  def code: String
}

abstract class EnumObject[EnumT <: EnumBase] {}

sealed abstract case class StateCode(code: String) extends EnumBase

object StateCode extends EnumObject[StateCode] {
  val CO = new StateCode("CO") {}
  val CA = new StateCode("CA") {}
}

case class ClassWithStateCode(id: Int, x: StateCode)

case class MyClass(id: Int, version: Int, year: Int, co: StateCode)

object MyEnum extends Enumeration {
  type WeekDay = Value
  val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}


sealed abstract class AbstractIdX(val id: String)
final case class OneId(override val id: String) extends AbstractIdX(id)
final case class TwoId(override val id: String) extends AbstractIdX(id)
final case class ThreeId(override val id: String) extends AbstractIdX(id)

case class TextWithId(id: AbstractIdX, text: String)
case class TextWithKnownId(id: ThreeId, text: String)

object StackoverflowAnswersJson4s {

  import org.json4s._
  import org.json4s.native.JsonMethods._
  import org.json4s.native.Serialization.{read, write}

  def main(args: Array[String]): Unit = {
    ex3()
  }

  def ex1() {
    println("STACKOVERFLOW")

    implicit val formats = DefaultFormats + FieldSerializer[Tweet]() + new EnumSerializer(MyEnum)

    val json = List(1, 2, 3)

    println(write(json))

    //    print(compact(render(Tweet(1, "ala ma kota", Instant.now()))))

    val json2 = parse("""{"id": 1, "text": "ala ma kota", "time": 12345.3}""")

    val tweet = json2.extract[Tweet]
    println(tweet)
    println(write(tweet))
    println(read[Tweet](write(tweet)))
  }

  def ex2() {

    class EnumSerializer[EnumT <: EnumBase : scala.reflect.ClassTag]
      extends org.json4s.Serializer[StateCode] {

      val EnumerationClass = scala.reflect.classTag[EnumT].runtimeClass

      override def deserialize(implicit format: Formats):
        PartialFunction[(TypeInfo, JValue), StateCode] = {
          case (TypeInfo(EnumerationClass, _), jsonx) => jsonx match {
            case i: JString => new StateCode(i.values) {}
          }
        }

      override def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
        case i: StateCode => JString(i.code)
      }
    }

    implicit val formats2 = DefaultFormats + new EnumSerializer[StateCode]


    val testClass = MyClass(2, 0, 1966, StateCode.CO)
    println(write(testClass))
    println(read[MyClass](write(testClass)))


    val testClass2 = MyClass(2, 0, 1966, StateCode.CA)
    println(write(testClass2))
    println(read[MyClass](write(testClass2)))

    val deser = read[MyClass](write(testClass2))
    println(deser.co == testClass2.co)
  }

  def ex3(): Unit = {

    class IdSerializer[T <: AbstractIdX : ClassTag](c: String => T) extends org.json4s.Serializer[T] {

      val EnumerationClass = scala.reflect.classTag[T].runtimeClass

      override def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), T] = {
        case (TypeInfo(EnumerationClass, _), jsonx) => jsonx match {
          case i: JString => c(i.values)
        }
      }

      override def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
        case i: T => JString(i.id)
      }
    }

    implicit val formats3 = DefaultFormats.withHints(ShortTypeHints(List(classOf[OneId], classOf[TwoId]))) +
      new IdSerializer[ThreeId](ThreeId.apply)
//    implicit val formats3 = DefaultFormats + IdSerializer

    val one = TextWithId(OneId("1"), "one!")
    val two = TextWithId(TwoId("2"), "two!")

    println(write(one))
    println(write(two))
    println(read[TextWithId](write(two)))

    val three = TextWithKnownId(ThreeId("3"), "three!")
    println(write(three))
    println(read[TextWithKnownId](write(three)))

  }
}

object StackoverflowAnswers {
  def main(args: Array[String]): Unit = {
    println("STACKOVERFLOW")
  }
}


object GnipProcessBuilder {
  type SetInt = Set[Int]

  import scala.sys.process._
  def runCommand(cmd: Seq[String]): (Int, String, String) = {
    val stdoutStream = new ByteArrayOutputStream
    val stderrStream = new ByteArrayOutputStream
    val stdoutWriter = new PrintWriter(stdoutStream)
    val stderrWriter = new PrintWriter(stderrStream)
    val exitValue = cmd.!(ProcessLogger(stdoutWriter.println, stderrWriter.println))
    stdoutWriter.close()
    stderrWriter.close()
    stdoutStream.close()
    stderrStream.close()
    (exitValue, stdoutStream.toString, stderrStream.toString)
  }

  def main(args: Array[String]): Unit = {
    println("process builder")
    (1 to 10000).foreach { i =>
      println(runCommand(List("/home/kpbochenek/github/kpbochenek/appartments-play/ops.py")))
    }

    val x: SetInt = Set()
  }
}


object TypeSafeConfig {
  def main(args: Array[String]): Unit = {
    println("TYPESAFE CONFIG")

    val c1: Config = ConfigFactory.parseString("x.a = 3 \n x.b = 'bbb' \n x.c = [1, 2, 3]")
    val c2: Config = ConfigFactory.parseString("x.a = 4")

    println(c1)
    println("-----------")
    println(c2)

    println(c1.getInt("x.a"))
    println(c2.withValue("x.c", c1.getList("x.c")))
  }
}

object SynchronizedDeadlock {
  def main(args: Array[String]): Unit = {
//    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.blocking
    println("SYNC ISSUE")

    val lock = new Object()

    implicit val ec = ExecutionContext.fromExecutor(new ThreadPoolExecutor(30, 40, 300, TimeUnit.SECONDS, new ArrayBlockingQueue(1000)))

    def processInFuture = {
      println("START!!")
      lock.synchronized {
        // simulate async call made blocking
        Await.result(Future {
          blocking {
            println("Sleeping")
            Thread.sleep(100)
            println("Waking")
          }
        }, Duration.Inf)
      }
    }

    // fire off 10 async events and wait on each one
    (0 until 10).
      map(_ => Future { processInFuture }).
      foreach(future => Await.result(future, Duration.Inf))

    println("KONIEC ;))))")
  }
}


object CustomCountSort {
  case class T(x: Int, desc: String)

  trait Intable[T] {
    def getInt(v: T): Int
  }

  def customSort[T : Intable : ClassTag](l: List[T], maxSize: Int): List[T] = {
    val ma: Intable[T] = implicitly[Intable[T]]

    val Count = Array.ofDim[Int](maxSize)

    for (e <- l) {
      Count(ma.getInt(e)) += 1
    }

    var total = 0
    for (i <- 0 until maxSize) {
      val oldCount = Count(i)
      Count(i) = total
      total += oldCount
    }

    val output: Array[T] = Array.ofDim[T](l.size)

    for (e <- l) {
      output(Count(ma.getInt(e))) = e
      Count(ma.getInt(e)) += 1
    }

    output.toList
  }

  def main(args: Array[String]): Unit = {
    println("CUSTOM SORT")
    val l = List(T(1, "1"), T(2, "1"), T(1, "2"), T(7, "1"), T(4, "1"), T(1, "3"), T(5, "1"), T(2, "2"), T(1, "4"))

    implicit val TOps = new Intable[T] {
      override def getInt(v: T): Int = v.x
    }

    println(l.sortWith { case (x,y) => x.x < y.x})
    println(customSort(l, 10))
  }
}


object ContinuousPassingStyle {

  class ProcessingUnit {
//    def process[T](): Future[T] = {
//      val a = computeA()
//      val b = computeB()
//
//      val c = computeC(a, b)

//    }
  }


  def main(args: Array[String]): Unit = {
    print(List(1, 2, 5, 4, 2).sorted)

  }
}
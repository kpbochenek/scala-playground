import sbt._

object Dependencies {
  val scalazV = "7.2.16"
  val scalaTestV = "3.0.4"


  val scalazDeps = Seq("org.scalaz" %% "scalaz-core" % scalazV)

  val scalaTestDeps = Seq("org.scalatest" %% "scalatest" % scalaTestV % Test)
}
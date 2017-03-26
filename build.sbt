
lazy val root = project.in(file("."))
  .aggregate(sub1, sub2, FreeMonad)

lazy val sub1 = project.in(file("sub1"))
lazy val sub2 = project.in(file("sub2")).dependsOn(sub1 % "compile->compile;test->test")
lazy val FreeMonad = project.in(file("FreeMonad")).settings(
  libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"
)

name := "scala-playground"
scalaVersion := "2.12.1"

javacOptions := Seq("-source", "1.8", "-target", "1.8", "-g")
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")
scalacOptions in Test ++= Seq("-Yrangepos")



libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.5.0",
  "org.json4s" %% "json4s-ext" % "3.5.0",

  "org.scalaz"        %% "scalaz-core"   % "7.2.9",
  "org.typelevel"     %% "cats"          % "0.9.0",

  "org.specs2"        %% "specs2-core"   % "3.8.8"  % "test"
)

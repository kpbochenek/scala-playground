import sbt._
import Dependencies._
import sbt.Keys.scalacOptions


name := "scala-playground"
scalaVersion := "2.12.4"
javacOptions := Seq("-source", "1.8", "-target", "1.8", "-g")
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")
scalacOptions in Test ++= Seq("-Yrangepos")


lazy val root = project.in(file("."))
  .aggregate(scalaz)

lazy val scalaz = project.in(file("scalaz"))
    .settings(
      libraryDependencies ++= scalazDeps ++ scalaTestDeps
    )


libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.5.0",
  "org.json4s" %% "json4s-ext" % "3.5.0",

  "org.scalaz"        %% "scalaz-core"   % "7.2.9",
  "org.typelevel"     %% "cats"          % "0.9.0",

  "org.specs2"        %% "specs2-core"   % "3.8.8"  % "test"
)

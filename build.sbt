
lazy val root = project.in(file("."))
  .aggregate(sub1, sub2)

lazy val sub1 = project.in(file("sub1"))
lazy val sub2 = project.in(file("sub2")).dependsOn(sub1)


name := "scala-playground"
scalaVersion := "2.11.8"

javacOptions := Seq("-source", "1.8", "-target", "1.8", "-g")
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")
scalacOptions in Test ++= Seq("-Yrangepos")



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",

  "org.json4s" %% "json4s-native" % "3.4.0",
  "org.json4s" %% "json4s-ext" % "3.4.0",


  "org.scalaz.stream" %% "scalaz-stream" % "0.8",
  "org.specs2"        %% "specs2-core"   % "3.7"  % "test"
)

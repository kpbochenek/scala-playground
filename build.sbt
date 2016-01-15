name := "scala-playground"
scalaVersion := "2.11.7"

javacOptions := Seq("-source", "1.8", "-target", "1.8", "-g")
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")
scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
  "org.scalaz.stream" %% "scalaz-stream" % "0.8",
  "org.specs2"        %% "specs2-core"   % "3.7"  % "test"
)

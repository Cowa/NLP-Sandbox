lazy val root = (project in file(".")).settings(
 name := "alignment",
 version := "1.0.0",
 scalaVersion := "2.11.7"
)

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.3"

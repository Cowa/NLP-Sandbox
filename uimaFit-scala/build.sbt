name := "uimaFit-scala"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies += "com.github.jenshaase.uimascala" %% "uimascala-core" % "0.5.0"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

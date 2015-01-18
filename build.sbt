organization := "net.abesto"

name := "Duolingo-To-Anki"

version := "0.2.1"

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.json4s" %% "json4s-native" % "3.2.6",
  "org.scala-lang" % "scala-swing" % scalaVersion.value
  )

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

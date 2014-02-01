organization := "net.abesto"

name := "Duolingo-To-Anki"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.json4s" %% "json4s-native" % "3.2.6"
  )

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

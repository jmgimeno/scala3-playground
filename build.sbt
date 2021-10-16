
ThisBuild / scalaVersion := "3.0.0"
ThisBuild / version := "1.0.0"

lazy val `scala3-playground` =
  project
    .in(file("."))
    .aggregate(`tagless-final`, scalawithcats, zionomicon, scalacheck)

lazy val basic =
  project
    .in(file("basic"))

lazy val zionomicon =
  project
    .in(file("zionomicon"))
    .settings(
      libraryDependencies ++= Seq(
        "com.novocode" % "junit-interface" % "0.11" % "test",
        "dev.zio" %% "zio" % "1.0.9"
      )
    )

lazy val scalawithcats =
  project
    .in(file("scalawithcats"))
    .settings(
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-core" % "2.6.1"
      )
    )

lazy val `tagless-final` =
  project
    .in(file("tagless-final"))
    .settings(
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-effect" % "3.2.8"
      )
    )

lazy val scalacheck =
  project
    .in(file("scalacheck"))
    .settings(
      libraryDependencies ++= Seq(
        "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"
      )
    )

package zionomicon.chapter2

import java.io.PrintWriter

import zio.*

// 1

def readFile(file: String): String =
  val source = scala.io.Source.fromFile(file)
  try source.getLines().mkString finally source.close()

def readFileZio(file: String): Task[String] =
  ZIO.effect(readFile(file))

@main def exercise1: Unit =
  val runtime = Runtime.default
  val result = runtime.unsafeRun(readFileZio("build.sbt"))
  print(result)

// 2

def writeFile(file: String, text: String): Unit =
  import java.io.*
  val pw = PrintWriter(File(file))
  try pw.write(text) finally pw.close

def writeFileZio(file: String, text: String): Task[Unit] =
  ZIO.effect(writeFile(file, text))

@main def exercise2: Unit =
  val runtime = Runtime.default
  val result = runtime.unsafeRun(writeFileZio("potato.txt", "potato"))


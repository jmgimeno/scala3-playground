package zionomicon.chapter2

object Exercises {

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
}

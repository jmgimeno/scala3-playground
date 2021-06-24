package chapter1

trait Printable[A]:
  def format(a: A): String

object Printable:
  def format[A](a: A)(using printable: Printable[A]): String =
    printable.format(a)

  def print[A](a: A)(using printable: Printable[A]): Unit =
    println(format(a))

  given catPrintable: Printable[Cat] with
    def format(cat: Cat) =
      s"${cat.name} is a ${cat.age} year-old ${cat.color} cat"

object PrintableOps:

  extension [A] (a: A)
    def format(using printable: Printable[A]): String =
      Printable.format(a)

  extension [A] (a: A)
    def print(using printable: Printable[A]): Unit =
      Printable.print(a)

final case class Cat(name: String, age: Int, color: String)

@main def main(): Unit =

  val cat = Cat("Buffy", 16, "grey")

  import Printable.*
  print(cat)

  import PrintableOps.*
  cat.print




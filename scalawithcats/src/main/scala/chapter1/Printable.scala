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

final case class Cat(name: String, age: Int, color: String)

@main def main(): Unit =
  import Printable.*
  val cat = Cat("Buffy", 16, "grey")
  print(cat)




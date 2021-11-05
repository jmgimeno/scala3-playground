package fix

import cats.{Defer, Eval}
import cats.Defer.*

object Factorial {

  def fact(n: Int): Int =
    if n == 0 then 1
    else n * fact(n - 1)

  def factF(recur: Int => Int) = (n: Int) =>
    if n == 0 then 1
    else n * recur(n - 1)

  // Not stack-safe
  val factFix = Defer[[A] =>> Function1[Int, A]].fix[Int](factF)

  def main(args: Array[String]): Unit = {
    println(fact(10))
    println(factFix(10))
  }
}

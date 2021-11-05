package fix

import cats.{Defer, Eval}
import cats.Defer.*

object Factorial {

  def fact(n: Int): BigInt =
    if n == 0 then 1
    else n * fact(n - 1)

  def factF(recur: Int => Eval[BigInt]): Int => Eval[BigInt] = (n: Int) =>
    if n == 0 then Eval.now(1)
    else Eval.defer {
      recur(n - 1).map(_ * n)
    }

  // Stack-safe
  val factFix = Defer[[A] =>> Function1[Int, A]].fix(factF)

  def main(args: Array[String]): Unit = {
    //println(fact(10_000))
    println(factFix(10_000).value)
  }
}

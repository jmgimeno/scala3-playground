package fix

import cats.{Applicative, Defer, Eval}
import cats.Defer.*
import cats.effect.IO
import cats.syntax.applicative.*
import cats.syntax.functor.*

object Factorial {

  def fact(n: Int): BigInt =
    if n == 0 then 1
    else n * fact(n - 1)

  def absFact[F[_] : Defer : Applicative](n: Int): F[BigInt] =
    if (n == 0) then BigInt(1).pure
    else Defer[F].defer {
      absFact(n - 1).map(_ * n)
    }

  def factE(n: Int): Eval[BigInt] =
    if (n == 0) then Eval.now(1)
    else Eval.defer {
      factE(n - 1).map(_ * n)
    }

  def factF(recur: Int => Eval[BigInt]): Int => Eval[BigInt] = (n: Int) =>
    if n == 0 then Eval.now(1)
    else Eval.defer {
      recur(n - 1).map(_ * n)
    }

  // Stack-safe
  val factFixF = Defer[[A] =>> Function1[Int, A]].fix(factF)

  def factIO(n: Int): IO[BigInt] = n match {
    case 0 => IO.pure(1)
    case n => IO.defer {
      factIO(n - 1).map {
        m => m * n
      }
    }
  }

  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global

    //println(fact(10_000))
    println(factE(10_000).value)
    println("-" * 30)
    println(factFixF(10_000).value)
    println("-" * 30)
    println(absFact[Eval](10_000).value)
    println("-" * 30)
    println(factIO(10_000).unsafeRunSync())
    println("-" * 30)
    println(absFact[IO](10_000).unsafeRunSync())
  }
}

// Monads are Monoids in the Category of Endofunctors
//   from RockTheJVM
//   https://youtu.be/CMm98RkCgPg

import MonoidsInCategoryOfEndofunctors.ListSpecialMonoid

object MonoidsInCategoryOfEndofunctors {

  // Higher kinded type monoid = monoid in the category of T[_]
  trait MonoidInCategoryK2[T[_], ~>[_[_], _[_]], U[_], P[_]] {
    def unit: U ~> T

    def combine: P ~> T
  }

  // Monoid in a monoidal category = monoid in the category of T
  trait MonoidIncategory[T, ~>[_, _], U, P] {
    def unit: U ~> T

    def combine: P ~> T
  }

  trait GeneralMonoid[T, U, P] extends MonoidIncategory[T, Function1, U, P] {
    // def unit: U => T
    // def combine: P => T
  }

  trait FunctionalMonoid[T] extends GeneralMonoid[T, Unit, (T, T)] {
    // def unit: Unit => T
    // def combine: (T, T) => T
  }

  trait Monoid[T] extends FunctionalMonoid[T] {
    // public API
    def empty: T

    def combine(a: T, b: T): T

    // hidden API
    def unit = _ => empty

    def combine = t => combine(t._1, t._2)
  }

  // endofunctors
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  given functorList: Functor[List] with {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  // value transformations are functions
  trait ValueTransformation[-A, +B] {
    def apply(a: A): B
  }

  // functor transformations are natural transformations
  trait FunctorNatTransformation[-F[_], +G[_]] {
    def apply[A](fa: F[A]): G[A]
  }

  // Ex: .headOption, .toList, .toOption, .toTry
  object ListToOptionNatTrans extends FunctorNatTransformation[List, Option] {
    override def apply[A](fa: List[A]): Option[A] = fa.headOption
  }

  // The Id functor
  type Id[A] = A

  given idFunctor: Functor[Id] with {
    override def map[A, B](fa: A)(f: A => B): B = f(fa)
  }

  // composing Functors
  def funcComposition[A, B, C](f: A => B, g: B => C, a: A) = g(f(a))

  type HKTComposition[F[_], G[_], A] = G[F[A]]
  type SameTypeComposition[F[_], A] = F[F[A]]

  trait MonoidInCategoryOfFunctors[F[_] : Functor]
    extends MonoidInCategoryK2[F, FunctorNatTransformation, Id, [A] =>> F[F[A]]] {

    type FunctorProduct[A] = F[F[A]]
    // def unit: FunctorNatTransformation[Id, F]
    // def combine: FunctorNatTransformation[FunctorProduct, F]

    def pure[A](a: A): F[A] = unit(a)

    def flatMap[A, B](fa: F[A], f: A => F[B]): F[B] = {
      val functor = summon[Functor[F]]
      val ffb: F[F[B]] = functor.map(fa)(f)
      combine(ffb)
    }
  }

  object ListSpecialMonoid extends MonoidInCategoryOfFunctors[List] {
    override def unit: FunctorNatTransformation[Id, List] =
      new FunctorNatTransformation[Id, List] {
        override def apply[A](fa: A): List[A] = List(fa)
      }

    // FunctorProduct[A] = List[List[A]]
    override def combine: FunctorNatTransformation[FunctorProduct, List] =
      new FunctorNatTransformation[FunctorProduct, List] {
        override def apply[A](fa: List[List[A]]): List[A] = fa.flatten
      }
  }

  // Now we can go backwards
  trait Monad[F[_]] extends Functor[F] with MonoidInCategoryK2[F, FunctorNatTransformation, Id, [A] =>> F[F[A]]] {
    // public API
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    // functor API
    def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(f andThen pure)

    // auxiliar method
    def flatten[A](ffa: F[F[A]]): F[A] = flatMap(ffa)(identity)

    type FunctorProduct[A] = F[F[A]]

    def unit: FunctorNatTransformation[Id, F] =
      new FunctorNatTransformation[Id, F] {
        override def apply[A](fa: Id[A]): F[A] = pure(fa)
      }

    def combine: FunctorNatTransformation[FunctorProduct, F] =
      new FunctorNatTransformation[FunctorProduct, F] {
        override def apply[A](fa: F[F[A]]): F[A] = flatten(fa)
      }
  }

  def main(args: Array[String]): Unit = {
    println(ListSpecialMonoid.combine(List(
      List(1, 2, 3),
      List(4, 5, 6),
      List(7, 8, 9)
    )))
  }
}

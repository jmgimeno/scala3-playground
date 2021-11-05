// From SIP-35: Opaque types
// https://docs.scala-lang.org/sips/opaque-types.html

package opaques.semigroups

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

object Semigroup {
  def instance[A](f: (A, A) => A): Semigroup[A] =
    new Semigroup[A] {
      def combine(x: A, y: A): A = f(x, y)
    }
}

type Id[A] = A

trait Wrapping[F[_]] {
  def wraps[G[_], A](ga: G[A]): G[F[A]]

  def unwraps[G[_], A](ga: G[F[A]]): G[A]
}

abstract class Wrapper[F[_]] {
  self =>
  def wraps[G[_], A](ga: G[A]): G[F[A]]

  def unwraps[G[_], A](gfa: G[F[A]]): G[A]

  final def apply[A](a: A): F[A] = wraps[Id, A](a)

  given Wrapping[F] with {
    def wraps[G[_], A](ga: G[A]): G[F[A]] = self.wraps(ga)

    def unwraps[G[_], A](ga: G[F[A]]): G[A] = self.unwraps(ga)
  }
}

opaque type First[A] = A

object First extends Wrapper[First] {
  def wraps[G[_], A](ga: G[A]): G[First[A]] = ga

  def unwraps[G[_], A](gfa: G[First[A]]): G[A] = gfa

  given [A]: Semigroup[First[A]] =
    Semigroup.instance((x, y) => x)
}

opaque type Last[A] = A

object Last extends Wrapper[Last] {
  def wraps[G[_], A](ga: G[A]): G[Last[A]] = ga

  def unwraps[G[_], A](gfa: G[Last[A]]): G[A] = gfa

  given [A]: Semigroup[Last[A]] =
    Semigroup.instance((x, y) => y)
}

opaque type Min[A] = A

object Min extends Wrapper[Min] {
  def wraps[G[_], A](ga: G[A]): G[Min[A]] = ga

  def unwraps[G[_], A](gfa: G[Min[A]]): G[A] = gfa

  given [A](using o: Ordering[A]): Semigroup[Min[A]] =
    Semigroup.instance(o.min)
}

opaque type Max[A] = A

object Max extends Wrapper[Max] {
  def wraps[G[_], A](ga: G[A]): G[Max[A]] = ga

  def unwraps[G[_], A](gfa: G[Max[A]]): G[A] = gfa

  given [A](using o: Ordering[A]): Semigroup[Max[A]] =
    Semigroup.instance(o.max)
}

opaque type Plus[A] = A

object Plus extends Wrapper[Plus] {
  def wraps[G[_], A](ga: G[A]): G[Plus[A]] = ga

  def unwraps[G[_], A](gfa: G[Plus[A]]): G[A] = gfa

  given [A](using n: Numeric[A]): Semigroup[Plus[A]] =
    Semigroup.instance(n.plus)
}

opaque type Times[A] = A

object Times extends Wrapper[Times] {
  def wraps[G[_], A](ga: G[A]): G[Times[A]] = ga

  def unwraps[G[_], A](gfa: G[Times[A]]): G[A] = gfa

  given [A](using n: Numeric[A]): Semigroup[Times[A]] =
    Semigroup.instance(n.times)
}

opaque type Reversed[A] = A

object Reversed extends Wrapper[Reversed] {
  def wraps[G[_], A](ga: G[A]): G[Reversed[A]] = ga

  def unwraps[G[_], A](gfa: G[Reversed[A]]): G[A] = gfa

  given [A](using o: Ordering[A]): Ordering[Reversed[A]] =
    o.reverse
}

opaque type Unordered[A] = A

object Unordered extends Wrapper[Unordered] {
  def wraps[G[_], A](ga: G[A]): G[Unordered[A]] = ga

  def unwraps[G[_], A](gfa: G[Unordered[A]]): G[A] = gfa

  given [A]: Ordering[Unordered[A]] =
    Ordering.by(_ => ())
}


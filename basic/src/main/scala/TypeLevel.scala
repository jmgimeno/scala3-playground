/*
  Inspired by
  - https://blog.rockthejvm.com/type-level-programming-scala-3/
  - https://blog.rockthejvm.com/type-level-quicksort/
*/

object TypeLevel {

  trait Nat
  trait Zero extends Nat
  trait Succ[N <: Nat] extends Nat

  type _0 = Zero
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  trait <[A <: Nat, B <: Nat]
  object < {
    given basic[A <: Nat]: <[Zero, Succ[A]] with {}
    given inductive[A <: Nat, B <: Nat](using A < B) : <[Succ[A], Succ[B]] with {}
  }

  trait +[A <: Nat, B <: Nat, R <: Nat]
  object + {
    given basic[A <: Nat]: +[Zero, A, A] with {}
    given inductive[A <: Nat, B <: Nat, R <: Nat](using +[A, B, R]): +[Succ[A], B, Succ[R]] with {}
  }

  trait Vect[L <: Nat]
  trait Nil extends Vect[Zero]
  trait Cons[H <: Nat, L <: Nat, T <: Vect[L]] extends Vect[Succ[L]]

  trait ConcatVect[L1 <: Nat, L2 <: Nat, L <: Nat, V1 <: Vect[L1], V2 <: Vect[L2], V <: Vect[L]]
  object ConcatVect {
    given [L1 <: Nat, L2 <: Nat, L <: Nat, V1 <: Vect[L1], V2 <: Vect[L2], V <: Vect[L]]
      (using +[L1, L2, L]): ConcatVect[L1, L2, L, V1, V2, V] with {}
    def apply[L1 <: Nat, L2 <: Nat, L <: Nat, V1 <: Vect[L1], V2 <: Vect[L2], V <: Vect[L]]
      (using concatVect: ConcatVect[L1, L2, L, V1, V2, V]): ConcatVect[L1, L2, L, V1, V2, V] = concatVect
  }

  val concat = ConcatVect[_1, _1, _2, Cons[_0, _0, Nil], Cons[_0, _0, Nil], Cons[_0, _1, Cons[_0, _0, Nil]]]

}

package opaques

import semigroups.*

object Main {

  def reduce[A](l: List[A])(using sg: Semigroup[A]) =
    l.reduce(sg.combine)

  def reduce_v2[W[_], A](l: List[A])(wr: Wrapper[W])(using sg: Semigroup[W[A]]) =
    wr.wraps(l).reduce(sg.combine)

  def reduce_v3[W[_], A](l: List[A])(using wr: Wrapping[W], sg: Semigroup[W[A]]) =
    wr.wraps(l).reduce(sg.combine)

  def main(args: Array[String]): Unit = {
    val l = List(1,2,3,4)
    val lPlus = Plus.wraps(l)
    println(reduce(lPlus))
    val lTimes = Times.wraps(l)
    println(reduce(lTimes))
    val lMin = Min.wraps(l)
    println(reduce(lMin))

    println("-" * 30)

    println(reduce_v2(l)(Plus))
    println(reduce_v2(l)(Times))

    println("-" * 30)

    println(reduce_v3[Plus, Int](l))
    println(reduce_v3[Times, Int](l))

    println("-" * 30)

    val uPlus = Plus(1)
    val vPlus = Plus(2)
    val result = summon[Semigroup[Plus[Int]]].combine(uPlus, vPlus)
    println(result)
  }
}

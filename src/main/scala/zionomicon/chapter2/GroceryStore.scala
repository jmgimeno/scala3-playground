package zionomicon.chapter2

import zio.*

val goShopping =
  ZIO.effect(println("Going to the grocery store"))

import zio.clock.*
import zio.duration.*

val goShoppingLater =
  goShopping.delay(1.seconds)

val goShoppingRepeat =
  goShoppingLater.repeatN(5)

object GroceryStore extends App:
  def run(args: List[String]) =
    goShoppingRepeat.exitCode


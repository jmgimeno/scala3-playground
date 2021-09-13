import cats.data.State

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Ref
import cats.effect.kernel.Sync
import cats.effect.std.Console
import cats.*
import cats.implicits.*

trait Counter[F[_]]:
  def incr: F[Unit]
  def get: F[Int]

def mainCounter[F[_] : Sync](): F[Counter[F]] =
  Ref[F].of(0) map { ref =>
    new Counter[F] :
      def incr: F[Unit] = ref.update(_ + 1)

      def get: F[Int] = ref.get
  }

def program[F[_] : Console : Sync](c: Counter[F]) : F[Unit] =
  for
    _ <- Console[F].println("Init counter")
    _ <- c.incr.replicateA(100)
    v <- c.get
    _ <- Console[F].println(s"Counter value is $v")
  yield ()

object Main extends IOApp.Simple:
  val run = for
    c <- mainCounter[IO]()
    _ <- program(c)
  yield ()

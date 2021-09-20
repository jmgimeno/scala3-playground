import cats.data.StateT

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

def testCounter[F[_] : Applicative]: Counter[[X] =>> StateT[F, Int, X]] =
  new Counter[[X] =>> StateT[F, Int, X]]:
    def incr : StateT[F, Int, Unit] = StateT { counter =>
      (counter+1, ()).pure
    }
    def get: StateT[F, Int, Int] = StateT { counter =>
      (counter, counter).pure
    }

def program[F[_] : Console : Monad](c: Counter[F]) : F[Unit] =
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

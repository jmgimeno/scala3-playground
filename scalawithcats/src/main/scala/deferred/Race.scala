package deferred

import cats.effect.kernel.{Deferred, Fiber, Outcome}
import cats.effect.{IO, IOApp}
import scala.concurrent.duration.*

object Race extends IOApp.Simple {

  extension[A] (io: IO[A])
    def debug: IO[A] = for {
      a <- io
      t = Thread.currentThread().getName
      _ = println(s"[$t] $a")
    } yield a

  type RaceResult[A, B] = Either[
    (Outcome[IO, Throwable, A], Fiber[IO, Throwable, B]), // (winner result, loser fiber)
    (Fiber[IO, Throwable, A], Outcome[IO, Throwable, B]) // (loser fiber, winner result)
  ]

  def ourRacePair[A, B](ioa: IO[A], iob: IO[B]): IO[RaceResult[A, B]] =
    for {
      signal <- Deferred[IO, Either[Outcome[IO, Throwable, A], Outcome[IO, Throwable, B]]]
      fiba <- ioa.guaranteeCase(a => signal.complete(Left(a)).void).start
      fibb <- iob.guaranteeCase(b => signal.complete(Right(b)).void).start
      result <- signal.get
    } yield result match {
      case Left(a) => Left((a, fibb))
      case Right(b) => Right((fiba, b))
    }

  def createIO(id: Int, t: Int): IO[Int] =
    IO(s"Running: $id").debug >>
      IO.sleep(t.seconds) >>
      IO(s"Done: $id").debug >>
      IO(id)

  //override def run: IO[Unit] = ourRacePair(createIO(7, 1), createIO(9, 2)).debug.void

  val leak = for {
    _ <- (IO("leak").debug >> IO.sleep(1.second)).foreverM.start
    f <- (IO("not leak").debug >> IO.sleep(500.millis)).foreverM.start
    _ <- (IO.sleep(5.seconds) >> IO("cancelling") >> f.cancel)
  } yield()

  override def run = for {
    _ <- leak
    _ <- (IO("other things") >> IO.sleep(1.minute))
  } yield ()
}

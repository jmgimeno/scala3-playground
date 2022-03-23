package misc

import cats.effect.{IO, IOApp}
import cats.effect.kernel.Fiber
import cats.syntax.traverse.*

import scala.concurrent.duration.*

object Example1 extends IOApp.Simple {
  val a, b, c: IO[Unit] = IO.sleep(1.second)
  val r1 = a.productR(b).productR(c).start  // 3 seconds
  val r2 = a.productR(b).productR(c.start)  // 1 seconds
  val r3 = a.productR(b.productR(c).start)  // 2 seconds

  val r4 = (a >> b >> c).start // 3 seconds
  val r5 = a >> b >> c.start   // 1 seconds
  val r6 = a >> (b >> c).start // 2 seconds

  def program(r: IO[Fiber[IO, Throwable, Unit]]): IO[(Duration, Duration)] = for {
    t0 <- IO.realTime
    f <- r
    t1 <- IO.realTime
    _ <- f.join
    t2 <- IO.realTime
  } yield (t1 - t0, t2 - t1)

  override def run: IO[Unit]
    = List(r1, r2, r3, r4, r5, r6).traverse(program(_).flatMap(IO.println)).void
}

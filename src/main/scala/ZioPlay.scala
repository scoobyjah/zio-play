import zio._
import scala.util.Random

sealed trait ZioPlayException extends Throwable
class FlakyPrintException(message: String) extends ZioPlayException

object ZioPlay extends App {


  def run(args: List[String]) =
    mainExecution.fold(_ => 1, _ => 0) // some convenience logic to return an effect type that can be executed by the main
    //mainExecution.retry(Schedule.once).fold(_ => 1, _ => 0) actually does retry

  val mainExecution =
    for {
      _    <- flakyPrint("writing")
      _    <- flakyPrint("to")
      _    <- flakyPrint("the")
      _    <- flakyPrint("console")
      _    <- flakyPrint("is", successRate = 0.4f).orElse(flakyPrint("isn't?"))
      _    <- flakyPrint("complicated", successRate = 0.2f).retry(Schedule.recurs(10))
    } yield ()


  def flakyPrint(text: String, successRate: Float = 1f) = {
    val rand = UIO(Random.nextFloat)

    rand.flatMap(random => {
      if (random < successRate) {
        IO.succeed(println(text))
      }
      else
      //IO.fail(new FlakyPrintException("Uh Oh"))
      throw new RuntimeException("Uh Oh")  // try this instead to demo what an unchecked error looks line in zio's tracing
    })
  }

  def notFlaky(text: String, successRate: Float = 1f) = {
    val rand = Random.nextFloat //PROBLEM!!!!! the ZIO is now impure - it always fails or always succeeds, we can't retry that
    if (rand < successRate )
      IO.succeed(println(text))
    else
      IO.fail(new FlakyPrintException("Uh Oh"))
  }

}
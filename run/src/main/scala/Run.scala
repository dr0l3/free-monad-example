import Interpreter._
import Model.MyApp
import Model.dsl._
import cats.free.Free
import cats.~>
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dr0l3 on 5/26/17.
  */
object Run {
	def program()(implicit M: Reads[MyApp], P: Writes[MyApp]): Free[MyApp, String] = {
		import M._
		import P._
		for {
			name <- read()
			_ <- write(name)
			_ <- writeExcitedly(name)
		} yield name
	}
	
	def main(args: Array[String]): Unit = {
		val interpreter: MyApp ~> Future = ReadLogger.andThen(FutureReader) or WriteLogger.andThen(FutureWriter)
		val name = program().foldMap[Future](interpreter)
		println(s"End of world: $name")
	}
}


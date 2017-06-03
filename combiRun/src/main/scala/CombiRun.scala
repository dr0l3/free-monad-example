import Algebra._
import MoreOpsAlgebra.{MoreOperations, MoreOpsSyntax}
import OpsAlgebra._
import OpsInterpreter._
import SmallInterpreter._
import cats.{Id, ~>}
import cats.data.Coproduct
import cats.free.Free
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dr0l3 on 6/3/17.
  */
object CombiRun {
	type MyApp[A] = Coproduct[Console, Operations, A]
	type BiggerApp[A] = Coproduct[Console, Coproduct[Operations, MoreOperations, String],A]
	def program(stuff: String)(implicit C: ConsoleSyntax[MyApp,String], O: OperationsSyntax[MyApp,String]): Free[MyApp, String] = {
		import C._, O._
		for {
			input <- C.read()
			transformed <- O.intersperse(input, stuff, 2)
			_ <- C.print(transformed)
		} yield transformed
	}
	
	def treeWayProgram(stuff: String)(implicit C: ConsoleSyntax[BiggerApp,String], O:OperationsSyntax[BiggerApp,String], M: MoreOpsSyntax[BiggerApp,String]): Free[BiggerApp,String] = {
		import C._, O._, M._
		for {
			input <- C.read()
			transform <- O.intersperse(input,stuff,0)
			again <- M.prepend(transform, "heheheh")
			_ <- C.print(again)
		} yield again
	}
	
	def main(args: Array[String]): Unit = {
		val interpreter: MyApp ~> Id = ConsoleLogger.andThen(TestConsoleInterpreter) or OperationsLogger.andThen(OpsInterpreter.Interpreter)
		val output = program("hehehe").foldMap(interpreter)
		println(output)
		
		println("-" * 50)
		
		val biggerInterpreter: BiggerApp ~> Id = ConsoleLogger.andThen(TestConsoleInterpreter) or (OperationsLogger.andThen(OpsInterpreter.Interpreter) or MoreOpsInterpreter.Logger.andThen(MoreOpsInterpreter.Interpreter))
		val biggerOutput = treeWayProgram("yep").foldMap(biggerInterpreter)
		println(biggerOutput)
	}
	
}

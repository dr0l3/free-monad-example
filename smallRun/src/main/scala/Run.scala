import Algebra.{Console, ConsoleSyntax}
import SmallInterpreter.{ConsoleLogger, TestConsoleInterpreter}
import cats.{Id, ~>}
import cats.free.Free

/**
  * Created by dr0l3 on 5/28/17.
  */
object Run {
	def program()(implicit C: ConsoleSyntax[Console,String]): Free[Console,Unit] = {
		import C._
		for {
			string <- read()
			_ <- print(string)
		} yield ()
	}
	
	def longerProgram()(implicit CS: ConsoleSyntax[Console, String]): Free[Console,Unit] = {
		import CS._
		for {
			string <- CS.read()
			_ <- CS.print(string)
			someOtherString <- CS.read()
			_ <- CS.print(someOtherString)
			_ <- CS.print(string + someOtherString)
			number <- CS.readInt()
			_ <- CS.print(number.toString)
		} yield ()
	}
	
	def main(args: Array[String]): Unit = {
		val interpreter: Console ~> Id = ConsoleLogger.andThen(TestConsoleInterpreter)
		program().foldMap(interpreter)
		
		println("-" * 30)
		
		longerProgram().foldMap(interpreter)
	}
}

import Interpreter._
import Model.MyApp
import Model.dsl._
import cats.free.Free
import cats.{Id, ~>}
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dr0l3 on 5/26/17.
  */
object Run {
	def program()(implicit M: Reads[MyApp], P: Writes[MyApp,String]): Free[MyApp, String] = {
		import M._
		import P._
		for {
			name <- read()
			_ <- write(name)
			_ <- writeExcitedly(name)
		} yield name
	}
	
	def programWithInts()(implicit A: Reads[MyApp], P:Writes[MyApp,Int]): Free[MyApp,Int] = {
		import A._,P._
		for  {
			number <- readInt()
			_ <- write(number)
			_ <- writeExcitedly(number)
		} yield number
	}
	
	def programCombined()(implicit R: Reads[MyApp], I: Writes[MyApp,Int], S: Writes[MyApp,String]): Free[MyApp,String] ={
		import R._, I._,S._
		for {
			number  <- readInt()
			_       <- I.write(number)
			_       <- I.writeExcitedly(number)
			name    <- read()
			_       <- S.write(name)
			_       <- S.writeExcitedly(name)
		} yield name
	}
	
	def main(args: Array[String]): Unit = {
		val anotherInterpreter: MyApp ~> Id = ReadLogger.andThen(TestReadInterpreter) or WriteLogger.andThen(WriteInterpreter)
		val anotherName: Id[String] = program().foldMap(anotherInterpreter)
		println(s"End of world: $anotherName")
		
		println("----------------------------------------------")
		val someInt: Id[Int] = programWithInts().foldMap(anotherInterpreter)
		println(someInt)
		
		println("----------------------------------------------")
		val someString: Id[String] = programCombined().foldMap(anotherInterpreter)
		println(someString)
		
		println("----------------------------------------------")
		
		val futureInterpreter: MyApp ~> Future = ReadLogger.andThen(FutureReader) or WriteLogger.andThen(FutureWriter)
		val name: Future[String] = program().foldMap[Future](futureInterpreter)
		println(s"End of world: $name")
	}
}



import Example._
import Example.dsl._
import cats.data.Coproduct
import cats.free.{Free, Inject}
import cats.{Id, ~>}

import scala.language.higherKinds

/**
  * Created by dr0l3 on 5/21/17.
  */
object Example {
	type MyApp[A] = Coproduct[Reader, Writer, A]
	
	sealed trait Reader[A]
	case class Read() extends Reader[String]
	
	sealed trait Writer[A]
	case class Write(line:String) extends Writer[Unit]
	case class WriteExcitedly(line: String)extends Writer[Unit]
	
	object dsl {
		class Reads[F[_]](implicit I: Inject[Reader, F]) {
			def read(): Free[F, String] = Free.inject[Reader, F](Read())
		}
		
		object Reads {
			implicit def reads[F[_]](implicit I: Inject[Reader, F]): Reads[F] = new Reads[F]
		}
		
		class Writes[F[_]](implicit I: Inject[Writer, F]) {
			def write(line: String): Free[F, Unit] = Free.inject[Writer, F](Write(line))
			def writeExcitedly(line: String) : Free[F, Unit] = Free.inject[Writer, F](WriteExcitedly(line))
		}
		
		object Writes {
			implicit def writes[F[_]](implicit I: Inject[Writer, F]): Writes[F] = new Writes[F]
		}
	}
}

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
		val interpreter: MyApp ~> Id = ReadLogger.andThen(TestReadInterPreter) or WriteLogger.andThen(WriteInterpreter)
		val name = program().foldMap(interpreter)
		println(s"End of world: $name")
	}
}


object WriteLogger extends (Writer ~> Writer) {
	override def apply[A](fa: Writer[A]): Writer[A] ={
		fa match {
			case Write(line) => println(s"******* Writing: $line *******")
			case WriteExcitedly(line) => println(s"******* Exictedly logging : $line *******")
		}
		fa
	}
}

object ReadLogger extends (Reader ~> Reader) {
	override def apply[A](fa: Reader[A]): Reader[A] ={
		fa match {
			case Read() => println("******* Trying to read input *******")
		}
		fa
	}
}

object TestReadInterPreter extends (Reader ~> Id){
	override def apply[A](fa: Reader[A]): Id[A] = fa match {
		case Read() => "Test input"
	}
}

object ReadInterpreter extends (Reader ~> Id){
	override def apply[A](fa: Reader[A]): Id[A] = fa match {
		case Read() => scala.io.StdIn.readLine()
	}
}

object WriteInterpreter extends (Writer ~> Id){
	override def apply[A](fa: Writer[A]): Id[A] = fa match {
		case Write(line)=> println(line)
		case WriteExcitedly(line) => println(s"$line!!!")
	}
}
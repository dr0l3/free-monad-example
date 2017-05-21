import Example._
import Example.dsl._
import cats.data.Coproduct
import cats.free.{Free, Inject}
import cats.{Id, ~>}
import cats.implicits._

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
	
	object dsl {
		class Reads[F[_]](implicit I: Inject[Reader, F]) {
			def read(): Free[F, String] = Free.inject[Reader, F](Read())
		}
		
		object Reads {
			implicit def reads[F[_]](implicit I: Inject[Reader, F]): Reads[F] = new Reads[F]
		}
		
		class Writes[F[_]](implicit I: Inject[Writer, F]) {
			def write(line: String): Free[F, Unit] = Free.inject[Writer, F](Write(line))
		}
		
		object Writes {
			implicit def writes[F[_]](implicit I: Inject[Writer, F]): Writes[F] = new Writes[F]
		}
	}
}

object Run {
	def program()(implicit M: Reads[MyApp], P: Writes[MyApp]): Free[MyApp, Unit] = {
		import M._, P._
		for {
			name <- read()
			_ <- write(name)
		} yield ()
	}
	
	def main(args: Array[String]): Unit = {
		val interpreter: MyApp ~> Id = TestReadInterPreter or WriteInterpreter
		program().foldMap(interpreter)
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
	}
}
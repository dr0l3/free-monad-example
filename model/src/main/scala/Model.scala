import cats.data.Coproduct
import cats.free.{Free, Inject}
import io.circe.generic._
import io.circe.syntax._
/**
  * Created by dr0l3 on 5/26/17.
  */
object Model {
	
	
	sealed trait Reader[A]
	case class Read() extends Reader[String]
	case class ReadInt() extends Reader[Int]
	
	sealed trait Writer[A]
	case class Write[A](line:A) extends Writer[Unit]
	case class WriteExcitedly[A](line: A)extends Writer[Unit]
	
	type MyApp[A] = Coproduct[Reader, Writer, A]
	
	object dsl {
		class Reads[F[_]](implicit I: Inject[Reader, F]) {
			def read(): Free[F, String] = Free.inject[Reader, F](Read())
			def readInt(): Free[F,Int] = Free.inject[Reader,F](ReadInt())
		}
		
		object Reads {
			implicit def reads[F[_],A](implicit I: Inject[Reader, F]): Reads[F] = new Reads[F]
		}
		
		class Writes[F[_],A](implicit I: Inject[Writer, F]) {
			def write(line: A): Free[F, Unit] = Free.inject[Writer, F](Write(line))
			def writeExcitedly(line: A) : Free[F, Unit] = Free.inject[Writer, F](WriteExcitedly(line))
		}
		
		object Writes {
			implicit def writes[F[_],A](implicit I: Inject[Writer, F]): Writes[F,A] = new Writes[F,A]
		}
	}
	
	def main(args: Array[String]): Unit = {
		val a= Write("hello")
		val b= WriteExcitedly("hello")
		println(a.asJson.noSpaces)
		println(b.asJson.noSpaces)
	}
}
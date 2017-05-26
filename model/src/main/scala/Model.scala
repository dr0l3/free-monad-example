import cats.data.Coproduct
import cats.free.{Free, Inject}

/**
  * Created by dr0l3 on 5/26/17.
  */
object Model {
	sealed trait Reader[A]
	case class Read() extends Reader[String]
	
	
	sealed trait Writer[A]
	case class Write(line:String) extends Writer[Unit]
	case class WriteExcitedly(line: String)extends Writer[Unit]
	
	type MyApp[A] = Coproduct[Reader, Writer, A]
	
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
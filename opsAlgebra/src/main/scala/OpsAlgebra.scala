import cats.free.{Free, Inject}
import io.circe.generic.JsonCodec

import scala.language.higherKinds

/**
  * Created by dr0l3 on 6/3/17.
  */
object OpsAlgebra {
	@JsonCodec sealed trait Operations[A]
	@JsonCodec case class Intersperse[A](orig: A, toInsert: A, spaces: Int) extends Operations[A]
	object Operations
	
	class OperationsSyntax[F[_], A](implicit I: Inject[Operations, F]) {
		def intersperse(orig: A, toInsert: A, spaces: Int): Free[F,A] = Free.inject[Operations,F](Intersperse[A](orig, toInsert, spaces))
	}
	object OperationsSyntax {
		implicit def ops[F[_],A](implicit I: Inject[Operations, F]): OperationsSyntax[F,A] = new OperationsSyntax[F,A]
	}
	
}

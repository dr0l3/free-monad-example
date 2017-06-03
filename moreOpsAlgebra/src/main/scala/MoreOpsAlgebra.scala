import cats.free.{Free, Inject}

/**
  * Created by dr0l3 on 6/3/17.
  */
object MoreOpsAlgebra {
	sealed trait MoreOperations[A]
	case class Prepend[String](orig: String, toPrepend: String) extends MoreOperations[String]
	
	class MoreOpsSyntax[F[_],A](implicit I: Inject[MoreOperations,F]) {
		def prepend(orig: String, toPrepend: String): Free[F,String] = Free.inject[MoreOperations,F](Prepend(orig,toPrepend))
	}
	
	object MoreOpsSyntax {
		implicit def moreOpsSyntax[F[_],A](implicit I: Inject[MoreOperations,F]): MoreOpsSyntax[F,A] = new MoreOpsSyntax[F,A]
	}
}

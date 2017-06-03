import MoreOpsAlgebra.{MoreOperations, Prepend}
import cats.{Id, ~>}

/**
  * Created by dr0l3 on 6/3/17.
  */
object MoreOpsInterpreter {
	object Interpreter extends (MoreOperations ~> Id) {
		override def apply[A](fa: MoreOperations[A]): Id[A] = fa match {
			case Prepend(orig, toPrepend) => (toPrepend.asInstanceOf[String]+orig.asInstanceOf[String]).asInstanceOf[A]
		}
	}
	
	object Logger extends (MoreOperations ~> MoreOperations){
		override def apply[A](fa: MoreOperations[A]): MoreOperations[A] = {
			println(s"*** $fa ***")
			fa
		}
	}
	
}

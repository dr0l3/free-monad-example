import cats.{Id, ~>}
import OpsAlgebra._

/**
  * Created by dr0l3 on 6/3/17.
  */
object OpsInterpreter {
	object Interpreter extends (Operations ~> Id){
		override def apply[A](fa: Operations[A]): Id[A] = fa match {
			case Intersperse(orig,toInsert,spaces) => (orig.asInstanceOf[String]+toInsert.asInstanceOf[String]).asInstanceOf[A]
		}
	}
	
	object OperationsLogger extends (Operations ~> Operations) {
		override def apply[A](fa: Operations[A]): Operations[A] = {
			println(s"*** $fa ***")
			fa
		}
	}
}

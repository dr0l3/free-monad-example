import Algebra._
import cats.{Id, ~>}

/**
  * Created by dr0l3 on 5/28/17.
  */
object SmallInterpreter {
	object TestConsoleInterpreter extends (Console ~> Id){
		override def apply[A](fa: Console[A]): Id[A] = fa match {
			case Read() => "hellow".asInstanceOf[A]
			case ReadInt() => 2.asInstanceOf[A]
			case Print(line) => println(line); line
		}
	}
	
	object ConsoleLogger extends (Console ~> Console) {
		override def apply[A](fa: Console[A]): Console[A] = {
			println(s"*** $fa ***")
			fa
		}
		
	}
	
}

import Model._
import argonaut.EncodeJson
import cats.{Id, ~>}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dr0l3 on 5/26/17.
  */
object Interpreter{
	object FutureReader extends (Reader ~> Future){
		override def apply[A](fa: Reader[A]): Future[A] = fa match {
			case Read() => Future.successful("Some string")
		}
	}
	
	object FutureWriter extends (Writer ~> Future) {
		override def apply[A](fa: Writer[A]): Future[A] = fa match {
			case Write(line) => Future(println(line))
			case WriteExcitedly(line) => Future(println(s"$line!!!"))
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
	
	object TestReadInterpreter extends (Reader ~> Id){
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
}
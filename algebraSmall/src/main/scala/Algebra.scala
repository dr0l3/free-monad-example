import cats.free.{Free, Inject}
import io.circe.generic._
import io.circe.syntax._

import scala.language.higherKinds

/**
  * Created by dr0l3 on 5/28/17.
  */
object Algebra {
	@JsonCodec sealed trait Console[A]
	@JsonCodec case class Print[A](line: A) extends Console[A]
	@JsonCodec case class Read[A]() extends Console[A]
	@JsonCodec case class ReadInt[A]() extends Console[A]
	object Console
	
	class ConsoleSyntax[F[_],A](implicit I: Inject[Console,F]){
		def print(line: A): Free[F,A] = Free.inject[Console, F](Print[A](line))
		def read(): Free[F,A] = Free.inject[Console, F](Read[A]())
		def readInt(): Free[F, Int] = Free.inject[Console, F](ReadInt())
		def readStr(): Free[F, String] = Free.inject[Console, F](Read[String]())
		def read2[B](): Free[F, B] = Free.inject[Console,F](Read[B]())
	}
	
	object ConsoleSyntax {
		implicit def consoleSyntax[F[_],A](implicit I: Inject[Console, F]): ConsoleSyntax[F,A] = new ConsoleSyntax[F,A]
	}
	
	def main(args: Array[String]): Unit = {
		val examplePrint: Console[String] = Print[String]("heheh")
		val exampleRead: Console[String] = Read[String]()
		val exampleIntRead: Console[Int]= Read[Int]()
		println(examplePrint.asJson.noSpaces)
		println(exampleRead.asJson.noSpaces)
		println(exampleIntRead.asJson.noSpaces)
	}
}
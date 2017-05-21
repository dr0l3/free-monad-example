
resolvers ++= Seq(
	Resolver.sonatypeRepo("releases"),
	Resolver.sonatypeRepo("snapshots")
)

name := "scala-implicits"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"
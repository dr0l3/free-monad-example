import sbt.Keys.{libraryDependencies, version}

def commonProject(projectName: String): Project = Project(projectName, file(projectName)).settings(
	name := projectName,
	version := "1.0",
	scalaVersion := "2.12.2",
	resolvers ++= Seq(
		Resolver.sonatypeRepo("releases"),
		Resolver.sonatypeRepo("snapshots")
	),
	addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
	libraryDependencies += "org.typelevel" %% "cats" % "0.9.0",
	libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2",
	libraryDependencies += "com.github.alexarchambault" %% "argonaut-shapeless_6.2" % "1.2.0-M4",
	libraryDependencies ++= Seq(
		"io.circe" %% "circe-core" % "0.8.0",
		"io.circe" %% "circe-generic" % "0.8.0",
		"io.circe" %% "circe-parser" % "0.8.0"))

lazy val model = commonProject("model")
lazy val smallAlgebra = commonProject("algebraSmall")

lazy val interpreter = commonProject("interpreter").dependsOn(model)
lazy val smallInterpreter = commonProject("interpreterSmall").dependsOn(smallAlgebra)

lazy val run = commonProject("run").dependsOn(model).dependsOn(interpreter)
lazy val smallRun = commonProject("smallRun").dependsOn(smallAlgebra).dependsOn(smallInterpreter)


import sbt.Keys.{libraryDependencies, version}

lazy val model = project.in(file("model")).settings(
	name := "model",
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
		"io.circe" %% "circe-parser" % "0.8.0"
	)
)

lazy val interpreter = project.in(file("interpreter")).dependsOn(model).settings(
	name := "interpreter",
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
		"io.circe" %% "circe-parser" % "0.8.0"
	)
)

lazy val run = project.in(file("run")).dependsOn(model).dependsOn(interpreter).settings(
	name := "run",
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
		"io.circe" %% "circe-parser" % "0.8.0"
	)
)


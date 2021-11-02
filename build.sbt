name := """play-test"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"
val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
libraryDependencies ++= Seq(
  guice,
  evolutions,
  jdbc,
  "net.codingwell"         %% "scala-guice"        % "5.0.2",
  "org.postgresql"         % "postgresql"          % "9.4-1206-jdbc42",
  "io.getquill"            %% "quill-jdbc"         % "3.10.0",
  "org.mockito"            %  "mockito-core"       % "2.27.0"    % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0"     % Test
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
routesImport += "models.DateRange"
routesImport += "binders.CustomBinders._"
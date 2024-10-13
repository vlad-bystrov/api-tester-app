import sbt.*

object Dependencies {
  // versions
  lazy val scalatestVersion = "3.2.18"
  lazy val log4catsVersion = "2.7.0"
  lazy val logbackVersion = "1.5.9"
  lazy val slf4jVersion = "2.0.13"
  lazy val catsEffectVersion = "3.5.4"
  lazy val pureConfigVersion = "0.17.7"
  lazy val mysqlVersion = "8.0.33"
  lazy val doobieVersion = "1.0.0-RC5"
  lazy val circeVersion = "0.14.9"
  lazy val http4sVersion = "0.23.27"

  // libraries
  val scalapbRuntime =
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"

  val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion
  val pureconfig = "com.github.pureconfig" %% "pureconfig-core" % pureConfigVersion
  val mysql = "mysql" % "mysql-connector-java" % mysqlVersion

  val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % Test

  val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j" % log4catsVersion,
    "org.slf4j" % "slf4j-simple" % slf4jVersion,
    "org.typelevel" %% "log4cats-noop" % log4catsVersion % Test,
    "ch.qos.logback" % "logback-classic" % logbackVersion % Test
  )

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion
  )

  val circe = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )

  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-ember-server" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )

  // projects
  val apiModelDeps = Seq(scalapbRuntime)

  val serverDeps = Seq(catsEffect, pureconfig, mysql) ++ logging ++ doobie ++ circe ++ http4s
}

import Dependencies.*

ThisBuild / organization := "com.tester"
ThisBuild / version      := "1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

lazy val apiModel = (project in file("modules/api-model"))
  .settings(
    name := "api-model",
    libraryDependencies ++= apiModelDeps
  )

lazy val server = (project in file("modules/server"))
  .settings(
    name := "server",
    libraryDependencies ++= serverDeps
  )

lazy val root = (project in file("."))
  .settings(
    name := "api-tester-server"
  )
  .aggregate(apiModel, server)
  .dependsOn(apiModel, server)

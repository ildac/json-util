import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  organization := "com.technicaleader",
  version := "1.0.0",
  scalaVersion := "2.11.8",
  coverageEnabled := true,
  resolvers := Seq(Resolver.mavenLocal
    , "Public Hortonworks Maven Repo" at "http://repo.hortonworks.com/content/groups/public/"
    , Resolver.sonatypeRepo("releases")
    , Resolver.typesafeRepo("releases")
    , Resolver.sbtPluginRepo("releases")
    , Resolver.bintrayRepo("scalaz", "releases")
    , Resolver.bintrayRepo("megamsys", "scala")
    , "Twitter Repository" at "http://maven.twttr.com/"
    , "jgit-repo" at "http://download.eclipse.org/jgit/maven"
  )
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "jsonUtil"
    , libraryDependencies ++= JsonUtil.dependencies
  )

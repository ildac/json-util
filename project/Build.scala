import sbt._

object Dependency {

  val jacksonCore: ModuleID = "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.8"
  val jackson: ModuleID = "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.8"


  // Test libs
  val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % "3.0.1" % "test" withSources()

}

object JsonUtil {

  import Dependency._

  val dependencies: Seq[ModuleID] = Seq(
    jackson,
    scalaTest
  )
}
package technicaleader.base

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

trait UnitSpecs extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with ScalaFutures


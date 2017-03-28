package technicaleader.test

import java.util.Calendar

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.technicaleader.base.UnitSpecs
import com.technicaleader.test.DwType.DwType
import com.technicaleader.test.ItemConstraint.ItemConstraint
import scala.reflect.runtime.universe._

case class JsonMessage(jobId: String, message: String)
case class NestedJsonMessage(jobId: String, message: String)

case class JobEntry(
                     metadataId: Int,
                     groupPK: Long,
                     jobType: String,
                     jobQualifier: String,
                     feed: Feed,
                     mapping: MetadataMapping
                   )

case class Feed(
                 ftpSite: String,
                 ftpFile: String,
                 header: Boolean,
                 columnSeparator: Option[String],
                 encoding: Option[String]
               )

case class MetadataMapping(
                            items: List[MetadataItems]
                          )

case class MetadataItems(
                          fieldName: String,
                          columnPosition: Int,
                          cast: String,
                          @JsonScalaEnumeration(classOf[DwTypeType]) dwType: DwType.DwType,
                          format: Option[String],
                          regex: Option[String],
                          scriptFunction: Option[String],
                          @JsonScalaEnumeration(classOf[ItemConstraintType]) itemConstraint: ItemConstraint.ItemConstraint
                        )

object ItemConstraint extends Enumeration {
  type ItemConstraint = Value
  val MANDATORY, OPTIONAL, CUSTOM = Value
}

object DwType extends Enumeration {
  type DwType = Value
  val DIMENSION, METRIC = Value
}

//object CsvFieldType extends Enumeration {
//  type CsvFieldType = Value
////  val STRING, LONG = Value
//  class CsvFieldTypeValue(value: Int, str: String) extends Val(value, str)
//
//  protected final def Values(value: Int, str: String): CsvFieldTypeValue = new CsvFieldTypeValue(value, str)
//
//  val LONG = Value("bigint")
//  val STRING = Value("text")
//
//}

class DwTypeType extends TypeReference[DwType.type]
class ItemConstraintType extends TypeReference[ItemConstraint.type]
//class CsvFieldTypeType extends TypeReference[CsvFieldType.type]

class JsonUtilTest extends UnitSpecs {

  "fromJson" should "parse the message string and instantiate a scala object" in {
    import com.technicaleader.MarshallableImplicits.Unmarshallable

    val jsonString = """{"jobId":"questo è il jobId","message":"questo è il messaggio"}"""

    val expectedResult = new JsonMessage("questo è il jobId", "questo è il messaggio")
    val result = jsonString.fromJson[JsonMessage]
    assert(result.equals(expectedResult))

  }

  it should "parse the message string and instantiate a nested scala object" in {
    import com.technicaleader.MarshallableImplicits.Unmarshallable

    val jsonString =
      """
        |{"metadataId":2,"groupPK":3000205,"jobType":"I_GENERIC_FTP","jobQualifier":"USERS",
        | "feed":{"ftpSite": "local","ftpFile":"users-20.csv","header":true,"columnSeparator":",","encoding":"UTF-8"},
        | "mapping":{
        |  "items":[
        |   {"fieldName":"idUser","columnPosition":0,"cast":"LONG","dwType":"METRIC","format":null,"regex":null,"scriptFunction":null,"itemConstraint":"MANDATORY"},
        |   {"fieldName":"idCompany","columnPosition":1,"cast":"LONG","dwType":"DIMENSION","format":null,"regex":null,"scriptFunction":null,"itemConstraint":"MANDATORY"},
        |   {"fieldName":"firstName","columnPosition":2,"cast":"STRING","dwType":"DIMENSION","format":null,"regex":null,"scriptFunction":null,"itemConstraint":"MANDATORY"},
        |   {"fieldName":"middleName","columnPosition":3,"cast":"STRING","dwType":"DIMENSION","format":null,"regex":null,"scriptFunction":null,"itemConstraint":"OPTIONAL"}
        |  ]
        | }
        |}
      """.stripMargin

    val expectedResult = JobEntry(
      2,
      3000205,
      "I_GENERIC_FTP",
      "USERS",
      Feed(
        "local",
        "users-20.csv",
        true,
        Some(","),
        Some("UTF-8")
      ),
      MetadataMapping(
        List[MetadataItems](
          MetadataItems(
            "idUser",
            0,
            "LONG",
            DwType.METRIC,
            None,
            None,
            None,
            ItemConstraint.MANDATORY
          ),
          MetadataItems(
            "idCompany",
            1,
            "LONG",
            DwType.DIMENSION,
            None,
            None,
            None,
            ItemConstraint.MANDATORY
          ),
          MetadataItems(
            "firstName",
            2,
            "STRING",
            DwType.DIMENSION,
            None,
            None,
            None,
            ItemConstraint.MANDATORY
          ),
          MetadataItems(
            "middleName",
            3,
            "STRING",
            DwType.DIMENSION,
            None,
            None,
            None,
            ItemConstraint.OPTIONAL
          )
        )
      )
    )

    val result = jsonString.fromJson[JobEntry]
    assert(result.equals(expectedResult))

  }

  "toJson" should "translate the scala object into a json string" in {
    import com.technicaleader.MarshallableImplicits.Marshallable

    val expectedResult: String = """{"jobId":"questo è il jobId","message":"questo è il messaggio"}"""

    val km = new JsonMessage("questo è il jobId", "questo è il messaggio")
    val result = km.toJson
    assert(result.equals(expectedResult))
  }

  "toMap" should "parse the message string and put it into a map[String, V]" in {
    import com.technicaleader.MarshallableImplicits.Unmarshallable

    val jsonString = """{"jobId":"questo è il jobId","message":"questo è il messaggio"}"""

    val expectedResult = Map[String, Any]("jobId" -> "questo è il jobId", "message" -> "questo è il messaggio")
    val result = jsonString.toMapOf[String]
    assert(result.equals(expectedResult))

  }

}
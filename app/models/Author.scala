package models

import java.time.LocalDate
import play.api.libs.json._

case class Author(id : Long, firstName: String, lastName : String, birthData : LocalDate)

object Author {
    implicit val authorFormat: OFormat[Author] = Json.format[Author]
}
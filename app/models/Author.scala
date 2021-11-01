package models

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.time.LocalDate
import play.api.libs.json._
import io.circe

case class AuthorDTO(firstName: String, lastName: String, birthDate: LocalDate)
case class Author(author_id : Long, firstName: String, lastName : String, birthDate : LocalDate)

object Author {

    implicit val decoder: Decoder[Author] = (c: HCursor) => {
        for {
            firstName <- c.downField("firstName").as[String]
            lastName <- c.downField("lastName").as[String]
            birthDate <- c.downField("birthDate").as[LocalDate]
        } yield {
            new Author(0L, firstName, lastName, birthDate)
        }
    }

    def fromJson(json : String) : Author = {
        decoder.decodeJson(circe.Json.fromString(json)) match {
            case Left(fail) => {
                println(fail.message)
                throw new JsonMappingException()
            }
            case Right(author) => author
        }
    }

    def fromDTOJson(authorDTOJson : String) : Author = {
        parser.decode[AuthorDTO](authorDTOJson) match {
            case Left(fail) => {
                println(fail.getMessage())
                throw new JsonMappingException()
            }
            case Right(dto) => new Author(0L, dto.firstName, dto.lastName, dto.birthDate)
        }
    }

    def toJson(input: List[Author]) : circe.Json = {
        input.asJson
    }
}
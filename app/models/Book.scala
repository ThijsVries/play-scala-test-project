package models

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.time.LocalDate

class JsonMappingException extends Exception

case class BookDTO(title : String, authorId : Long)
case class Book(book_id: Long, title : String, authorId : Long)

object Book {

    def fromJson(json : String) : Book = {
        parser.decode[Book](json) match {
            case Left(err) => {
                println(err.getMessage())
                throw new JsonMappingException()
            }
            case Right(book) => book
        }
    }

    def toJson(books: List[Book]) : Json = {
        books.asJson
    }
}

object BookDTO {
    def fromJson(json : String) : BookDTO = {
        parser.decode[BookDTO](json) match {
            case Left(err) => {
                throw new JsonMappingException
            }
            case Right(dto) => dto
        }
    }
}
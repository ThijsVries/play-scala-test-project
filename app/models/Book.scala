package models

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.time.LocalDate

class JsonMappingException extends Exception
case class Book(title : String, author : Author)

object Book {
    var books : List[Book] = List[Book](Book("Example book", Author(1L,"Foo", "Bar", LocalDate.of(1996,5,25))))

    def save(book : Book) = {
        books = books ::: List(book)
    }

    def toJson() : String = {
        books.asJson.noSpaces
    }

    def fromJson(json : String) : Unit = {
        parser.decode[Book](json) match {
            case Right(book) => save(book)
            case Left(ex) => throw new JsonMappingException
        }
    }
}
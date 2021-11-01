package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import dao.BookDao
import play.api.mvc.AbstractController
import models.Author
import models.Book
import models.BookDTO
import play.api.libs.json.Json
import models.JsonMappingException
import dao.AuthorDao
import dao.`package`._

@Singleton
class BookController @Inject()(val cc : ControllerComponents, val bookDao : BookDao, val authorDao : AuthorDao) extends AbstractController(cc) {

    def save() = Action(parse.json) { request => 
        try {
            val dto : BookDTO = BookDTO.fromJson(request.body.toString())
            if(authorDao.authorExists(dto.authorId)) {
                bookDao.save(dto)
                Ok(Json.obj("message" -> "Ok!"))
            } else {
                BadRequest(Json.obj("message" -> "Attempted to save a book with an author id that did not exist!"))
            }
            
        } catch {
            case e: JsonMappingException => BadRequest(Json.obj("message" -> "invalid json format!"))
            case e: MultipleResultsException => BadRequest(Json.obj("message" -> "Multiple authors were found for the given id, this should never be able to happen!"))
        }
    }

    def get(bookId : Long) = Action {
        Ok(Book.toJson(bookDao.get(bookId)).spaces2)
    }

    def update = Action(parse.json) { request => 
        try {
            val res = bookDao.update(Book.fromJson(request.body.toString()))
            Ok(Json.obj("message" -> s"Update successful: $res"))
        } catch {
            case e: JsonMappingException => BadRequest(Json.obj("message" -> "invalid json format!"))
        }
    }

    def delete(bookId : Long) = Action {
        val res = bookDao.delete(bookId)
        Ok(Json.obj("message" -> s"Delete successful: $res"))
    }

    def all() = Action { 
        Ok(Book.toJson(bookDao.all()).spaces2)
    }

    def byAuthor(authorId : Long) = Action {
        Ok(Book.toJson(bookDao.byAuthorId(authorId)).spaces2)
    }
}
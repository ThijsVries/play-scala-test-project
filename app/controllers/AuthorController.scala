package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import play.api.mvc.BaseController
import play.api.mvc.AbstractController
import models.Author
import models.JsonMappingException
import play.api.libs.json.Json
import dao.AuthorDao
import io.circe

@Singleton
class AuthorController @Inject() (val cc : ControllerComponents, val authorDao : AuthorDao) extends AbstractController(cc) {

    def save() = Action(parse.json) { request => 
        try {
            val auth = Author.fromDTOJson(request.body.toString())
            authorDao.insert(auth)
            Ok(Json.obj("message" -> "Success!"))
        } catch {
            case e: JsonMappingException => BadRequest(Json.obj("message" -> "Invalid Json format"))
        }
    }

    def all() = Action {
        Ok(Author.toJson(authorDao.all()).spaces2)
    }

    def getByFirstName(firstName : String) = Action { 
        val json : circe.Json = Author.toJson(authorDao.getAuthorByFirstName(firstName))
        Ok(json.spaces2)
    }
}
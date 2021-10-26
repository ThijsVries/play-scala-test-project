package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import models.Book
import play.api.libs.json.Writes
import play.api.libs.json.JsPath
import play.api.libs.json.JsError
import models.JsonMappingException
import models.AuthorRepository
import java.time.LocalDate
import models.Author
import scala.concurrent.ExecutionContext

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (val repo : AuthorRepository, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext)
    extends BaseController {

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method will be
    * called when the application receives a `GET` request with a path of `/`.
    */

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def test() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.create())
  }

  def saveAuthor() = Action {
    repo.create("Foo", "Bar", LocalDate.of(1996, 5, 25))
    Ok(Json.obj("message" -> "OK"))
  }

  def getAuthors() = Action.async { implicit request => 
    repo.list().map { author =>
      Ok(Json.toJson(author))
    }
  }

  def getBooks() = Action {
    Ok(Book.toJson())
  }

  def saveBook() = Action(parse.json) { request =>
    try {
      Book.fromJson(request.body.toString())
      Ok(Json.obj("message" -> "Success!"))
    } catch {
      case e: JsonMappingException => BadRequest(Json.obj("message" -> "Invalid json input"))
    }
  }
}

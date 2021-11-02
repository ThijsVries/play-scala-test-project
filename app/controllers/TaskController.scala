package controllers

import javax.inject.{Singleton, Inject}
import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController
import models.exceptions.JsonMappingException
import play.api.libs.json.Json
import models.TaskDTO
import models.Task
import models.DateRange
import dao.TaskDao
import dao.`package`.NoResultException
import dao.`package`.MultipleResultsException
import java.time.LocalDate

@Singleton
class TaskController @Inject()(val cc : ControllerComponents, val taskDao : TaskDao) extends AbstractController(cc) {

    def save() = Action(parse.json) { request => 
        try {
            val dto : TaskDTO = TaskDTO.fromJson(request.body.toString())
            val task : Task = Task(0L, dto.taskName, dto.taskDesc, dto.dueDate, false)
            taskDao.save(task)
            Ok(Json.obj("message" -> "Yes!"))
        } catch {
            case e: JsonMappingException => BadRequest(Json.obj("message" -> ":("))
        }
    }

    def all() = Action {
        Ok(Task.toJson(taskDao.all()).spaces2)
    }

    def get(taskId : Long) = Action {
        try {
            Ok(Task.toJson(taskDao.get(taskId)).spaces2)
        } catch {
            case e: NoResultException => BadRequest(Json.obj("message" -> "No task found for the given id!"))
            case e: MultipleResultsException => BadRequest(Json.obj("message" -> "Multiple tasks found for the given id, this should never be possible?!"))
        }
    }

    def update() = Action(parse.json) { request => 
        try {
            val task : Task = Task.fromJson(request.body.toString())
            taskDao.update(task)
            Ok(Json.obj("message" -> "Update succesfull!"))
        } catch {
            case e: JsonMappingException => BadRequest(Json.obj("message" -> "invalid json format!"))
        }
    }

    def delete(taskId : Long) = Action {
        val res = taskDao.delete(taskId)
        Ok(Json.obj("message" -> s"Delete success: $res"))
    }

    def between(range : DateRange) = Action {
        val from : LocalDate = range.from
        val to : LocalDate = range.to
        Ok(Task.toJson(taskDao.between(from,to)).spaces2)
    }

    def byStatus(done : Boolean) = Action {
        Ok(Task.toJson(taskDao.byStatus(done)).spaces2)
    }

    def done(taskId : Long) = Action {
        val res = taskDao.done(taskId)
        Ok(Json.obj("message" -> s"Update succerss: $res"))
    }
}
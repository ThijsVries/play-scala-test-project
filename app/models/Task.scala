package models

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.time.LocalDate
import models.exceptions.JsonMappingException

case class Task(taskId : Long, taskName : String, taskDesc : Option[String] = None, dueDate : Option[LocalDate] = None, done : Boolean)
case class TaskDTO(taskName : String, taskDesc : Option[String] = None, dueDate : Option[LocalDate] = None)

object Task {
    def toJson(tasks : List[Task]) : Json = {
        tasks.asJson
    }

    def toJson(task : Task) : Json = {
        task.asJson
    }

    def fromJson(json : String) : Task = {
        parser.decode[Task](json) match {
            case Left(fail) => {
                println(fail.getMessage())
                throw new JsonMappingException
            }
            case Right(task) => task
        }
    }
}

object TaskDTO {

    def fromJson(json : String) : TaskDTO = {
        parser.decode[TaskDTO](json) match {
            case Left(fail) => {
                println(fail.getMessage())
                throw new JsonMappingException
            }
            case Right(dto) => dto
        }
    }
}
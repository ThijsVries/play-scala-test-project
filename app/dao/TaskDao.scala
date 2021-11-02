package dao

import models.Task
import javax.inject.{Singleton, Inject}
import java.time.LocalDate

@Singleton
class TaskDao @Inject()(context : DBContext) {
    import context._

    private val taskTable = quote {
        querySchema[Task] (
            "task",
            _.taskId -> "task_id",
            _.taskName -> "task_name",
            _.taskDesc -> "task_description",
            _.dueDate -> "due_date",
            _.done -> "done"
        )
    }

    def save(task : Task) = {
        implicit val meta = insertMeta[Task](_.taskId)
        val q = quote {
            taskTable.insert(lift(task))
        }
        context.run(q)
    }

    def all() : List[Task] = {
        val q = quote {
            taskTable
        }
        context.run(q)
    }

    def get(taskId : Long) : Task = {
        val q = quote {
            taskTable.filter(a => a.taskId == lift(taskId))
        }

        val results : List[Task] = context.run(q)
        results.size match {
            case 0 => throw new NoResultException
            case 1 => results.head
            case _ => throw new MultipleResultsException
        }
    }

    def update(task : Task) : Boolean = {
        val q = quote {
            taskTable.filter(t => t.taskId == lift(task.taskId)).update(lift(task))
        }

        context.run(q) match {
            case 1 => true
            case 0 => false
        }
    }

    def between(from : LocalDate, to : LocalDate) : List[Task] = {
        val q = quote {
            taskTable.filter(t => t.dueDate.forall(d => d > lift(from) && d < lift(to)))
        }

        context.run(q)
    }

    def delete(taskId : Long) : Boolean = {
        val q = quote {
            taskTable.filter(t => t.taskId == lift(taskId)).delete
        }

        context.run(q) match {
            case 1 => true
            case 0 => false
        }
    }

    def byStatus(done : Boolean) : List[Task] = {
        val q = quote {
            taskTable.filter(t => t.done == lift(done))
        }

        context.run(q)
    }

    private implicit class LocalDateQuotes(left : LocalDate) {
        def >(right: LocalDate) = quote(infix"$left > $right".as[Boolean])
        def <(right: LocalDate) = quote(infix"$left < $right".as[Boolean])
    }
}


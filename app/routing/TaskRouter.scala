package routing

import javax.inject.Inject
import controllers.TaskController
import play.api.routing.SimpleRouter
import play.api.routing.Router
import play.api.mvc.Results
import play.api.routing._
import play.api.routing.sird._
import play.api.mvc.Action
import models.DateRange
import java.time.LocalDate

class TaskRouter @Inject()(taskController : TaskController) extends SimpleRouter {
    override def routes: Router.Routes = {
        case GET(p"/" ? q"status=$query") =>
            taskController.byStatus(query.toBoolean)
        case GET(p"/" ? q"from=$from" & q"to=$to") =>
            taskController.between(DateRange(LocalDate.parse(from), LocalDate.parse(to)))
        case GET(p"/$taskId/done") =>
            taskController.done(taskId.toLong)
        case POST(p"/") => 
            taskController.save()
        case PUT(p"/") => 
            taskController.update()
        case GET(p"/") =>
            taskController.all()
        case DELETE(p"/$taskId") =>
            taskController.delete(taskId.toLong)
    }
}
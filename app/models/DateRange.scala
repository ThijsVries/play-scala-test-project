package models

import java.time.LocalDate
import play.api.mvc.QueryStringBindable

case class DateRange(from : LocalDate, to : LocalDate)

object DateRangeBinder extends {
    implicit def queryStringBinder(implicit dateBinder : QueryStringBindable[LocalDate]) = new QueryStringBindable[DateRange] {

      override def bind(key: String, params: Map[String,Seq[String]]): Option[Either[String,DateRange]] = {
          for {
              from <- dateBinder.bind("from", params)
              to <- dateBinder.bind("to", params)
          } yield {
              (from, to) match {
                  case (Right(from), Right(to)) => Right(DateRange(from, to))
                  case _                        => Left("Unable to bind dateRange")
              }
          }
      }

      override def unbind(key: String, value: DateRange): String = {
          dateBinder.unbind("from", value.from) + "&" + dateBinder.unbind("to", value.to)
      }
    }
}
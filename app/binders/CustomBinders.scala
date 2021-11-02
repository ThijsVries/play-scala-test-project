package binders

import play.api.mvc.QueryStringBindable
import models.DateRange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CustomBinders {
    implicit def queryStringDateRangeBinder(implicit localDateBinder : QueryStringBindable[LocalDate]) = new QueryStringBindable[DateRange] {
        val format = "yyyy-MM-dd"

        override def bind(key: String, params: Map[String,Seq[String]]): Option[Either[String,DateRange]] = {
            for {
                from <- localDateBinder.bind("from", params)
                to <- localDateBinder.bind("to", params)
            } yield {
                (from, to) match {
                    case (Right(from), Right(to)) => Right(DateRange(from, to))
                    case _                        => Left("Failed to bind")
                }
            }
        }

        override def unbind(key: String, value: DateRange): String = {
            value.from.toString() + "&" + value.to.toString()
        }
    }

    implicit def queryStringLocalDateBinder = new QueryStringBindable[LocalDate] {

        override def bind(key: String, params: Map[String,Seq[String]]): Option[Either[String,LocalDate]] = {
            params(key).headOption map { dateString =>
                try {
                    Right(LocalDate.parse(dateString))
                } catch {
                    case e: IllegalArgumentException => Left("Failed to bind")
                }
            }
        }

        override def unbind(key: String, value: LocalDate): String = {
            value.toString()
        }
    }
}
package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.driver.JdbcProfile
import javax.inject
import java.time.LocalDate
import scala.concurrent.Future

@inject.Singleton
class AuthorRepository @Inject() (dbConfigProvider : DatabaseConfigProvider)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

    import dbConfig._
    import profile.api._

    class AuthorTable(tag : Tag) extends Table[Author](tag, "author") {
        def id = column[Long]("author_id",O.PrimaryKey, O.AutoInc)
        def firstName = column[String]("firstName")
        def lastName = column[String]("lastName")
        def birthDate = column[LocalDate]("birthDate")

        def * = (id, firstName, lastName, birthDate) <> ((Author.apply _).tupled, Author.unapply)
    }

    lazy val authors = TableQuery[AuthorTable]

    def create(firstname : String, lastName : String, birthDate : LocalDate) : Future[Author] = db.run {
        (authors.map(a => (a.firstName, a.lastName, a.birthDate))
            returning authors.map(_.id)
            into ((data, id) => Author(id, data._1, data._2, data._3))
        ) += (firstname, lastName, birthDate)
    }

    def list(): Future[Seq[Author]] = db.run {
        authors.result
    }
}
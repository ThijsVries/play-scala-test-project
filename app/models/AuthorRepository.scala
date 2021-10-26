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
    private val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

    import dbConfig._
    import profile.api._

    private class AuthorTable(tag : Tag) extends Table[Author](tag, "author") {
        def id = column[Long]("author_id",O.PrimaryKey, O.AutoInc)
        def firstName = column[String]("firstName")
        def lastName = column[String]("lastName")
        def birthDate = column[LocalDate]("birthDate")

        def * = (id, firstName, lastName, birthDate) <> ((Author.apply _).tupled, Author.unapply)
    }

    private val author = TableQuery[AuthorTable]

    def create(firstname : String, lastName : String, birthDate : LocalDate) : Future[Author] = db.run {
        (author.map(a => (a.firstName, a.lastName, a.birthDate))
            returning author.map(_.id)
            into ((data, id) => Author(id, data._1, data._2, data._3))
        ) += (firstname, lastName, birthDate)
    }

    def list(): Future[Seq[Author]] = db.run {
        author.result
    }
}
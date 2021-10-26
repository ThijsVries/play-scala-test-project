package models

import javax.inject
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext

@inject.Singleton
class BookRepository @inject.Inject() (dbConfigProvider : DatabaseConfigProvider,val authorRepo : AuthorRepository)(implicit ec : ExecutionContext) {
    val dbConfig = dbConfigProvider.get[slick.jdbc.JdbcProfile]

    import dbConfig._
    import profile.api._

    class BookTable(tag : Tag) extends Table[Book](tag, "book") {
        def id = column[Long]("book_id",O.PrimaryKey, O.AutoInc)
        def title = column[String]("title")
        def author_id = column[Long]("author_id")
        def * = (id, title, author_id) <> ((Book.apply _).tupled, Book.unapply)
        def author = foreignKey("author", author_id, authorRepo.authors)(_.id)
    }

    lazy val books = TableQuery[BookTable]
    
    def create(title : String, author_id : Long) = db.run {
        (books.map(b => (b.title, b.author_id))
            returning books.map(_.id)
            into ((data, id) => Book(id, data._1, data._2))
        ) += (title, author_id)
    }
}
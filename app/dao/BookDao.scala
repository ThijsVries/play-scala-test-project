package dao

import javax.inject.{Singleton, Inject}
import models.Book
import models.BookDTO
import models.Author

@Singleton
class BookDao @Inject()(context : DBContext) {
    import context._

    def save(bookDTO: BookDTO) = {
        val book = Book(0L, bookDTO.title, bookDTO.authorId)
        implicit val meta = insertMeta[Book](_.book_id)
        val q = quote(query[Book].insert(lift(book)))
        context.run(q)
    }

    def get(bookId : Long) : List[Book] = {
        val q = quote {
            query[Book].filter(b => b.book_id == lift(bookId))
        }

        context.run(q)
    }

    def update(book : Book) : Boolean = {
        val q = quote {
            query[Book].filter(_.book_id == lift(book.book_id)).update(lift(book))
        }

        context.run(q) match {
            case 1 => true
            case 0 => false
        }
    }

    def delete(bookId : Long) : Boolean = {
        val q = quote {
            query[Book].filter(_.book_id == lift(bookId)).delete
        }

        context.run(q) match {
            case 1 => true
            case 0 => false
        }
    }

    def all() : List[Book] = {
        val q = quote {
            query[Book]
        }

        context.run(q)
    }

    def byAuthorId(authorId : Long) : List[Book] = {
        val q = quote {
            query[Book].filter(b => b.authorId == lift(authorId))
        }

        context.run(q)
    }
}
package dao

import javax.inject
import models.Author
import io.getquill.Query


@inject.Singleton
class AuthorDao @inject.Inject()(context : DBContext) {

    import context._

    def insert(author : Author) = {
        implicit val meta = insertMeta[Author](_.author_id)

        val q = quote {
            query[Author].insert(lift(author))
        }

        context.run(q)
    }

    def all() : List[Author] = {
        val q = quote {
            query[Author]
        }

        context.run(q)
    }

    def getById(id: Long) : List[Author] = {
        val q = quote {
            query[Author].filter(a => a.author_id == lift(id))
        }

        context.run(q)
    }

    def getAuthorByFirstName(firstName : String) : List[Author] = {
        val q : Quoted[Query[Author]] = quote {
            query[Author].filter(a => a.firstName == (lift(firstName)))
        }

        context.run(q)
    }
}
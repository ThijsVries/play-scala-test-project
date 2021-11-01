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

    def get(id: Long) : Author = {
        val q = quote {
            query[Author].filter(a => a.author_id == lift(id))
        }

        val results : List[Author] = context.run(q)
        results.size match {
            case 0 => throw new NoResultException
            case 1 => results.head
            case _ => throw new MultipleResultsException
        }
    }

    def authorExists(id: Long) : Boolean = {
        val q = quote {
            query[Author].filter(a => a.author_id == lift(id)).size
        }

        context.run(q) match {
            case 0 => return false
            case 1 => return true
            case _ => throw new MultipleResultsException
        }
    }

    def getAuthorByFirstName(firstName : String) : List[Author] = {
        val q : Quoted[Query[Author]] = quote {
            query[Author].filter(a => a.firstName == (lift(firstName)))
        }

        context.run(q)
    }
}
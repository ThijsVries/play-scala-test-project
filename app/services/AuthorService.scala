package services

import javax.inject.Inject
import dao.AuthorDao
import java.time.LocalDate
import models.Author

class AuthorService @Inject()(authorDao : AuthorDao) {

    def createAuthor(firstName : String, lastName : String, birthDate : LocalDate) = {
        val author = Author(0L, firstName, lastName, birthDate)
        authorDao.insert(author)
    }
}
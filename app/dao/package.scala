import javax.inject.{Inject, Singleton}
import play.api.db.Database
import io.getquill.PostgresJdbcContext
import io.getquill.SnakeCase
import com.zaxxer.hikari.HikariDataSource

package object dao {
    @Singleton
    class DBContext @Inject()(db: Database) extends PostgresJdbcContext(SnakeCase, db.dataSource.asInstanceOf[HikariDataSource])

    class NoResultException extends Exception
    class MultipleResultsException extends Exception
}

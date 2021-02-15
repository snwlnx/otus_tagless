package tagless.dao

import cats.effect._
import cats.free.Free
import cats.implicits._
import doobie._
import doobie.free.connection
import doobie.implicits._
import doobie.h2._
import tagless.dao.DoobieBooksDao.{insertBooksSql, selectBooksSql}
import tagless.domain
import tagless.domain.Book

object DoobieBooksDao {

  val createBooksTable =
    sql"""
         |CREATE TABLE books (
         |   id INT NOT NULL,
         |   title VARCHAR(50) NOT NULL,
         |   author VARCHAR(20) NOT NULL
         |)""".stripMargin

  val selectBooksSql: Fragment = sql"select  id, title, author from books"

  val insertBooksSql = "INSERT INTO books (id, title, author) values(?, ?, ?)"

}

class DoobieBooksDao[F[_] : BracketThrow](transactor: H2Transactor[F]) extends BooksDao[F] {

  override def getBooks: F[Vector[domain.Book]] = {
    selectBooksSql.query[Book]
      .to[Vector]
      .transact(transactor)
  }

  override def addBooks(newBooks: Vector[domain.Book]): F[Unit] = {
    Update[Book](insertBooksSql)
      .updateMany(newBooks)
      .transact(transactor)
      .void
  }
}

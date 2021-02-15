package tagless

import cats.effect.{Async, ContextShift}
import tagless.dao.BooksDao
import tagless.domain.Book

import scala.concurrent.ExecutionContext

trait BooksHandler[F[_]] {
  def getBooks: F[Vector[Book]]

  def addBooks(books: Vector[Book]): F[Unit]
}

class BooksHandlerImpl[F[_] : Async : ContextShift](booksDao: BooksDao[F])
                                                   (implicit ec: ExecutionContext) extends BooksHandler[F] {
  override def getBooks: F[Vector[Book]] = {
    booksDao.getBooks
  }

  override def addBooks(books: Vector[Book]): F[Unit] = {
    booksDao.addBooks(books)
  }
}

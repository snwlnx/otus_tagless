package tagless.dao

import tagless.domain.Book

trait BooksDao[F[_]] {
  def getBooks: F[Vector[Book]]

  def addBooks(newBooks: Vector[Book]): F[Unit]
}

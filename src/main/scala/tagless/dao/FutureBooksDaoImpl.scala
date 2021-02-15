package tagless.dao

import tagless.domain.{Author, Book}

import java.util.concurrent.atomic.AtomicReference
import scala.concurrent.Future

class FutureBooksDaoImpl extends BooksDao[Future] {

  private val books = new AtomicReference(
    Vector(
      Book(1, "The Sorrows of Young Werther", Author("Johann Wolfgang von Goethe")),
      Book(2, "Iliad", Author("Homer")),
      Book(3, "Nad Niemnem", Author("Eliza Orzeszkowa")),
      Book(4, "The Colour of Magic", Author("Terry Pratchett")),
      Book(5, "The Art of Computer Programming", Author("Donald Knuth")),
      Book(6, "Pharaoh", Author("Boleslaw Prus"))
    )
  )

  override def getBooks: Future[Vector[Book]] =
    Future.successful(books.get())

  override def addBooks(newBooks: Vector[Book]): Future[Unit] =
    Future.successful(books.updateAndGet(v => v ++ newBooks))
}

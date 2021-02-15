package tagless

object domain {

  case class Author(name: String)

  case class Book(id: Int, title: String, author: Author)

}

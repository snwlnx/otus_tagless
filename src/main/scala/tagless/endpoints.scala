package tagless

import sttp.tapir.{Endpoint, emptyOutput, endpoint}
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI
import tagless.domain.Book
import sttp.tapir.generic.auto._
import io.circe.generic.auto._

object endpoints {

  val booksEndpoint: Endpoint[Unit, Unit, Vector[Book], Any] = endpoint.get
    .in("books")
    .in("list")
    .out(jsonBody[Vector[Book]])

  val addBooksEndpoint: Endpoint[Vector[Book], Unit, Unit, Any] = endpoint.post
    .in("books")
    .in("list")
    .in(jsonBody[Vector[Book]])
    .out(emptyOutput)

  // generating the documentation in yml; extension methods come from imported packages
  val openApiDocs: OpenAPI = OpenAPIDocsInterpreter.toOpenAPI(List(booksEndpoint, addBooksEndpoint),
    "The tapir library", "1.0.0")
}

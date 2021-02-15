package tagless

import cats.effect._
import cats.syntax.either._
import cats.syntax.semigroupk._
import cats.syntax.functor._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import tagless.dao.DoobieBooksDao
import tagless.domain.Book
import tagless.endpoints._
import zio.{URIO, ZEnv, ZIO}
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext.Implicits.global

object TapirHttp4s extends zio.App {

  def resources[F[_] : Async : ContextShift]: Resource[F, BooksHandlerImpl[F]] = for {
    cfg <- Resource.liftF(Config.loadConfig[F])
    tr <- Transactor.makeTransactor[F](cfg)
    dao = new DoobieBooksDao[F](tr)
    handler = new BooksHandlerImpl(dao)
  } yield handler

  def booksRoute[F[_] : Concurrent : ContextShift : Timer](booksHandler: BooksHandler[F]): HttpRoutes[F] = {
    Http4sServerInterpreter.toRoutes(booksEndpoint)(_ =>
      booksHandler.getBooks.map(Either.right[Unit, Vector[Book]])) <+>
      Http4sServerInterpreter.toRoutes(addBooksEndpoint)(books =>
        booksHandler.addBooks(books).map(Either.right[Unit, Unit])
      )
  } <+> new SwaggerHttp4s(openApiDocs.toYaml).routes[F]

  def start[F[_] : ConcurrentEffect : ContextShift : Timer] = {
    resources[F].use { handler =>
      val routes = booksRoute[F](handler)
      BlazeServerBuilder[F](global)
        .bindHttp(8080, "localhost")
        .withHttpApp(Router("/" -> routes).orNotFound)
        .serve
        .compile
        .lastOrError
    }
  }

  override def run(args: List[String]): URIO[ZEnv, zio.ExitCode] = {
    ZIO.runtime[ZEnv].flatMap { implicit rt =>
      start[zio.Task].exitCode
    }
  }
}
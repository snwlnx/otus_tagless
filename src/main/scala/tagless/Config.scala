package tagless

import cats.data.EitherT
import cats.effect.{Bracket, Sync}
import cats.syntax.flatMap._

import java.io.{File, FileReader}
import java.util.Scanner

case class Config(url: String, username: String, password: String)

object Config {

  def loadConfig[F[_] : Sync]: F[Config] = {
    val path = "src/main/resources/config.conf"
    val acquire: F[FileReader] = Files.open(path)
    val use: FileReader => F[String] = Files.read(_)
    val close: FileReader => F[Unit] = Files.close(_)

    Bracket[F, Throwable].bracket(acquire)(use)(close).flatMap { s =>
      EitherT.fromOption[F](fromString(s), new Exception("config is empty")).rethrowT
    }
  }

  private def fromString(str: String): Option[Config] = {
    val keys = str.split(";").map { tuple =>
      val s = tuple.split("=")
      (s.head, s.tail.head)
    }.toMap

    for {
      url <- keys.get("url")
      username <- keys.get("username")
      password <- keys.get("password")
    } yield Config(url, username, password)
  }
}


object Files {

  def open[F[_] : Sync](path: String): F[FileReader] = {
    Sync[F].delay(new FileReader(new File(path)))
  }

  def read[F[_] : Sync](fileReader: FileReader): F[String] = {
    Sync[F].delay {
      val sc = new Scanner(fileReader)
      sc.nextLine()
    }
  }

  def close[F[_] : Sync](fileReader: FileReader): F[Unit] = {
    Sync[F].delay(fileReader.close())
  }
}
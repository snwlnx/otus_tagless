name := "otus_tagless"

version := "0.1"

scalaVersion := "2.13.4"

val tapirVersion = "0.17.6"

libraryDependencies ++= Seq(

  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-redoc-akka-http" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirVersion,

  "org.tpolecat" %% "doobie-core" % "0.9.0",
  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2" % "0.9.0", // H2 driver 1.4.200 + type mappings.
  "org.tpolecat" %% "doobie-hikari" % "0.9.0", // HikariCP transactor.

  // https://mvnrepository.com/artifact/dev.zio/zio-interop-cats
  "dev.zio" %% "zio-interop-cats" % "2.2.0.1"
)

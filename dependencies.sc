import mill._, mill.scalalib._

object all {

	val hotpotato = ivy"com.github.jatcwang::hotpotato-core:0.1.1"
  val mockito = ivy"org.mockito:mockito-core:3.3.3"
  val postgres = ivy"org.postgresql:postgresql:42.2.14"
  val scalatest = ivy"org.scalatest::scalatest:3.2.0"

  object akka {
    val version = "2.6.6"
    val httpVersion = "10.1.12"

    val streams = ivy"com.typesafe.akka::akka-stream:$version"
    val http = ivy"com.typesafe.akka::akka-http:$httpVersion"
    val httpTestKit = ivy"com.typesafe.akka::akka-http-testkit:$httpVersion"
  }

  object cats {
    val core = ivy"org.typelevel::cats-core:2.1.1"
  }

  object circe {
    val version = "0.13.0"
    val core = ivy"io.circe::circe-core:$version"
    val generic = ivy"io.circe::circe-generic:$version"
    val akkaCompat = ivy"de.heikoseeberger::akka-http-circe:1.33.0"
  }

  object quill {
    val version = "3.5.2"

    val monix = ivy"io.getquill::quill-jdbc-monix:$version"
    val core = ivy"io.getquill::quill-core:$version"
  }

  object monix {
    val version = "3.2.2"
    val eval = ivy"io.monix::monix-eval:$version"
  }

  val testDependencies = Agg(
    scalatest,
    mockito,
    akka.httpTestKit
  )
}

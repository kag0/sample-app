import $file.dependencies
import dependencies.all._

@ 

import mill._, scalalib._

val scalaV = "2.13.1"

trait ServiceModule extends ScalaModule {
  def scalaVersion = scalaV

  trait ServiceTests extends Tests {
    def ivyDeps = testDependencies
    def testFrameworks = List("org.scalatest.tools.Framework")

    // by default tests run in a subprocess that doesn't inherit environment variables
    def forkEnv = T.input(T.ctx().env)

    def testOne(args: String*) = T.command {
      super.runMain("org.scalatest.run", args: _*)
    }
  }
}

object common extends ScalaModule {
  def scalaVersion = scalaV
	
	def ivyDeps = Agg(monix.eval, quill.core, hotpotato, circe.core)
}

object notes extends ServiceModule {
	def moduleDeps = List(common)
	def ivyDeps = Agg(postgres, quill.monix)
}

object api extends ScalaModule {
	def scalaVersion = scalaV
	
	def moduleDeps = List(notes)
  
  def ivyDeps = Agg(
		akka.http,
		akka.streams,
		circe.core,
		circe.akkaCompat,
		circe.generic
	)
}

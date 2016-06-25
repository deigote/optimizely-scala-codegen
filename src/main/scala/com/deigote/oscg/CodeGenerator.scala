package com.deigote.oscg

import java.nio.charset.StandardCharsets

object CodeGenerator {

	def main(args: Array[String]): Unit = main(args.toList)

	private def main(args: List[String]): Unit =
		main(
			args.toList.lift(0).getOrElse(throw new IllegalArgumentException(emptyArgumentError("experiments-url")))
		)

	private def main(experimentsUrl: String): Unit =
		println(fetchExperimentDefinitions(experimentsUrl))


	private def fetchExperimentDefinitions(url: String) =
		scala.io.Source.fromURL(url, StandardCharsets.UTF_8.toString).mkString


	private def emptyArgumentError(argument: String) =
		s"Error! The argument ${argument} cannot be empty. Usage: sbt \"run <experiments-url>\""

}

package com.deigote.oscg

import java.nio.charset.StandardCharsets

import org.jtwig.JtwigModel
import org.jtwig.JtwigTemplate
import scala.util.parsing.json.{JSONObject, JSON}
import scala.io.Source

object OptimizelyScalaCodeGenerator {

	def main(args: Array[String]): Unit = main(args.toList)

	private def main(args: List[String]): Unit =
		main(
			args.toList.lift(0).getOrElse(throw new IllegalArgumentException(emptyArgumentError("experiments-url"))),
			args.toList.lift(1).getOrElse(throw new IllegalArgumentException(emptyArgumentError("package")))
		)

	private def main(experimentsUrl: String): Unit = println((
		fetchExperimentsDefinition _ andThen
		JsonParser.parseObject
	)(experimentsUrl))

	private def fetchExperimentsDefinition(url: String): String =
		Source.fromURL(url, StandardCharsets.UTF_8.toString).mkString

	private def emptyArgumentError(argument: String) =
		s"""Error! The argument ${argument} cannot be empty. Usage: sbt "run <experiments-url> <package>""""

}

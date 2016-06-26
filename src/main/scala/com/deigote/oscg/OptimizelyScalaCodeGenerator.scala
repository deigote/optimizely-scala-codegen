package com.deigote.oscg

import java.nio.charset.StandardCharsets

import org.jtwig.JtwigModel
import org.jtwig.JtwigTemplate
import scala.util.parsing.json.{JSONObject, JSON}
import scala.io.Source

object OptimizelyScalaCodeGenerator {

	def main(args: Array[String]): Unit = main(args.toList)

	private def main(args: List[String]): Unit =
		main(Config(
			args.toList.lift(0).getOrElse(throw new IllegalArgumentException(emptyArgumentError("experiments-url"))),
			args.toList.lift(1).getOrElse(throw new IllegalArgumentException(emptyArgumentError("output-directory"))),
			args.toList.lift(2).getOrElse(throw new IllegalArgumentException(emptyArgumentError("package"))),
			args.toList.lift(3),
			args.toList.lift(4)
		))

	private def main(config: Config): Unit = (
		fetchExperimentsDefinition _ andThen
		JsonParser.parseObject andThen
		generateCode(config)
	)(config.experimentsUrl)

	private def fetchExperimentsDefinition(url: String): String =
		Source.fromURL(url, StandardCharsets.UTF_8.toString).mkString

	private def generateCode(config: Config)(root: JSONObject) =
		generators(config).foreach(_.generate(root))

	private def generators(config: Config): Set[CodeGenerator] = Set(
		new EventsGenerator(config.classesPackage, config.eventsClassName),
		new ExperimentsGenerator(config.classesPackage, config.experimentsClassName)
	)

	private def emptyArgumentError(argument: String) =
		s"""Error! The argument ${argument} cannot be empty. Usage: sbt "run <experiments-url> <output-directory> <package> [<events-class-name> <experiments-class-name>]""""

}

case class Config(experimentsUrl: String,
						outputDirectory: String,
						classesPackage: String,
						eventsClassName: Option[String],
						experimentsClassName: Option[String])

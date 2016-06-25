package com.deigote.oscg

import java.nio.charset.StandardCharsets

import scala.util.parsing.json.{JSONObject, JSON}
import scala.io.Source

object CodeGenerator {

	def main(args: Array[String]): Unit = main(args.toList)

	private def main(args: List[String]): Unit =
		main(
			args.toList.lift(0).getOrElse(throw new IllegalArgumentException(emptyArgumentError("experiments-url")))
		)

	private def main(experimentsUrl: String): Unit =
		println((fetchExperimentsDefinition _ andThen JsonObjectParser.apply)(experimentsUrl))

	private def fetchExperimentsDefinition(url: String): String =
		Source.fromURL(url, StandardCharsets.UTF_8.toString).mkString


	private object JsonObjectParser {

		def apply(jsonObject: String): JSONObject = parseJsonObject(jsonObject)

		private val jsonObject = classOf[JSONObject]

		private def parseJsonObject(definitionAsJson: String): JSONObject =
			JSON.parseRaw(definitionAsJson) match {
				case Some(json) => json.getClass match {
					case `jsonObject` => json.asInstanceOf[JSONObject]
					case _ => throw new IllegalArgumentException("the provided JSON has an array at top level but was expecting an object")
				}
				case None => throw new IllegalArgumentException("couldn't parse provided JSON")
			}
	}

	private def emptyArgumentError(argument: String) =
		s"""Error! The argument ${argument} cannot be empty. Usage: sbt "run <experiments-url>""""

}

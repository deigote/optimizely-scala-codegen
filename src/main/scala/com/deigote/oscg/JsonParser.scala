package com.deigote.oscg

import java.nio.charset.StandardCharsets

import scala.io.Source
import scala.util.parsing.json.{JSONType, JSONArray, JSON, JSONObject}

object JsonParser {

	private val jsonObject: Class[JSONObject] = classOf[JSONObject]
	private val jsonArray: Class[JSONArray] = classOf[JSONArray]

	def parseObject(definitionAsJson: String): JSONObject =
		(JSON.parseRaw _ andThen extractObject)(definitionAsJson)
			.getOrElse(throw new IllegalArgumentException(s"Couldn't parse ${definitionAsJson} as a JSON object"))

	def extractObject(raw: Option[Any]) = extract[JSONObject](raw, jsonObject)
	def extractArray(raw: Option[Any]) = extract[JSONArray](raw, jsonArray)

	private def extract[C](raw: Option[Any], classToExtract: Class[C]): Option[C] =
		raw.flatMap(json => json.getClass match {
			case `classToExtract` => Some(json.asInstanceOf[C])
			case _ => None
		})
}

package com.deigote.oscg

import scala.util.parsing.json.{JSON, JSONArray, JSONObject}

object JsonParser {

	private val jsonObject: Class[JSONObject] = classOf[JSONObject]
	private val jsonArray: Class[JSONArray] = classOf[JSONArray]

	def parseObject(definitionAsJson: String): JSONObject =
		JSON
			.parseRaw(definitionAsJson)
			.flatMap(extractObject)
			.getOrElse(throw new IllegalArgumentException(s"Couldn't parse ${definitionAsJson} as a JSON object"))

	def extractObject(raw: Any): Option[JSONObject] =
		extract[JSONObject](raw, jsonObject)
	def extractArray(raw: Any): Option[JSONArray] =
		extract[JSONArray](raw, jsonArray)

	private def extract[C](json: Any, classToExtract: Class[C]): Option[C] =
		json.getClass match {
			case `classToExtract` => Some(json.asInstanceOf[C])
			case _ => None
		}
}

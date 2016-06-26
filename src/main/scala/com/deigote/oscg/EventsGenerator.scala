package com.deigote.oscg

import scala.collection.JavaConversions.{seqAsJavaList, mapAsJavaMap}
import scala.util.parsing.json.JSONObject

class EventsGenerator(val classesPackage: String, eventsClassName: Option[String]) extends CodeGenerator {

	override val templateName: String = "Events"
	private val defaultClassName = "Event"

	override def model(rootObject: JSONObject): Map[String, Any] = {
		Map(
			"events" -> seqAsJavaList(getEventsAsList(rootObject)),
			"className" -> eventsClassName.getOrElse(defaultClassName)
		)
	}

	def getEventsAsList(rootObject: JSONObject) =
		rootObject
			.obj
			.get("events")
			.flatMap(JsonParser.extractArray)
			.getOrElse(throw new IllegalArgumentException("couldn't find a JSON array when getting the 'events' object"))
			.list
			.flatMap(JsonParser.extractObject)
	  		.map(_.obj)
		   .map(addClassNameFromKey _ andThen mapAsJavaMap)

}

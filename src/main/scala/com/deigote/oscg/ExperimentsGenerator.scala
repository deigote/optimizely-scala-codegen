package com.deigote.oscg

import java.util

import scala.collection.JavaConversions.{mapAsJavaMap, seqAsJavaList}
import scala.util.parsing.json.JSONObject
import java.util.{List => JList}

class ExperimentsGenerator(val classesPackage: String, experimentsClassName: Option[String]) extends CodeGenerator {

	override val templateName: String = "Experiments"
	private val defaultClassName = "Experiment"
	private val defaultVariationsClassName = "Variation"

	override def model(rootObject: JSONObject): Map[String, Any] = {
		Map(
			"experiments" -> seqAsJavaList(getExperimentsAsList(rootObject)),
			"className" -> experimentsClassName.getOrElse(defaultClassName),
			"variationsClassName" -> defaultVariationsClassName
		)
	}

	def getModelForVariations(variations: List[util.Map[String, Any]]): Map[String, Any] = Map(
		"variations" -> seqAsJavaList(variations),
		"variationsCsv" -> variations.map(variation => s""""${variation.get("key").toString}"""").mkString(", ")
	)

	private def getExperimentsAsList(rootObject: JSONObject) =
		rootObject
			.obj
			.get("experiments")
			.flatMap(JsonParser.extractArray)
			.getOrElse(throw new IllegalArgumentException("couldn't find a JSON array when getting the 'experiments' array"))
			.list
			.flatMap(JsonParser.extractObject)
	  		.map(_.obj)
		   .map(addClassNameFromKey)
			.map(model => model ++ Map("variationsClassName" -> model("className").toString.concat(defaultVariationsClassName)))
	  		.map(model => model ++ getModelForVariations(extractVariations(model)))
			.map(mapAsJavaMap)

	private def extractVariations(experimentsModel: Map[String, Any]) =
		experimentsModel
			.get("variations")
			.flatMap(JsonParser.extractArray)
			.getOrElse(throw new IllegalArgumentException(s"couldn't find a JSON array when getting the 'variations' array for experiment ${experimentsModel.get("key")}"))
			.list
			.flatMap(JsonParser.extractObject)
			.map(_.obj)
			.map(addClassNameFromKey _ andThen mapAsJavaMap)



}

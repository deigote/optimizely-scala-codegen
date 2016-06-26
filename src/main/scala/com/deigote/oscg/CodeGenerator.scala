package com.deigote.oscg

import org.jtwig.{JtwigModel, JtwigTemplate}
import scala.util.parsing.json.JSONObject

trait CodeGenerator {

	val classesPackage: String
	val templateName: String
	val templatesPath = "templates"

	def generate(rootObject: JSONObject) =
		template(templatesPath).render(
			toJtwigModel(commonModel(classesPackage) ++ model(rootObject)),
			System.out
		)

	protected def model(rootObject: JSONObject): Map[String, Any]

	protected def kebabToCamelCase(name: String): String =
		"-([a-z\\d])".r.replaceAllIn(name, m => m.group(1).toUpperCase())

	private def template(templatesPath: String): JtwigTemplate =
		JtwigTemplate.classpathTemplate(s"${templatesPath}/${templateName}.twig")

	private def commonModel(classesPackage: String): Map[String, Any] =
		Map("package" -> classesPackage)

	private def toJtwigModel(model: Map[String, Any]): JtwigModel =
		model.foldLeft(new JtwigModel)((model, entry) => model.`with`(entry._1, entry._2))

	protected def addClassNameFromKey(model: Map[String, Any]): Map[String, Any] =
		model ++
			model
				.get("key")
				.map(_.toString.capitalize)
				.map(kebabToCamelCase)
				.map(className => Map("className" -> className))
				.getOrElse(Map())

}

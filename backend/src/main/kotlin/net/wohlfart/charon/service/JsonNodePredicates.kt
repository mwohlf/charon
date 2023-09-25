package net.wohlfart.charon.service

import com.fasterxml.jackson.databind.JsonNode

abstract class Predicate : (JsonNode) -> Boolean {

    class True : Predicate() {
        override fun invoke(p1: JsonNode): Boolean {
            return true
        }
    }

    class Equals(private val fieldName: String, private val fieldValue: String) : Predicate() {

        constructor(fieldName: String, fieldValue: Long) : this(fieldName, fieldValue.toString())
        constructor(fieldName: String, fieldValue: Boolean) : this(fieldName, fieldValue.toString())

        override fun invoke(jsonNode: JsonNode): Boolean {
            return jsonNode.has(this.fieldName)
                && jsonNode[this.fieldName].asText() == fieldValue
        }
    }

    class Matches(private val fieldName: String, private val regex: Regex) : Predicate() {

        override fun invoke(jsonNode: JsonNode): Boolean {
            return jsonNode.has(this.fieldName)
                && regex.matches(jsonNode[this.fieldName].asText())
        }
    }

    class And(private vararg val predicates: Predicate) : Predicate() {

        override fun invoke(jsonNode: JsonNode): Boolean {
            return predicates.fold(true) { acc, next -> acc && next.invoke(jsonNode) }
        }
    }

}


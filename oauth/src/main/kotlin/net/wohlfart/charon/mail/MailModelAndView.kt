package net.wohlfart.charon.mail

import freemarker.core.CollectionAndSequence
import freemarker.template.*
import freemarker.template.TemplateHashModelEx2.KeyValuePairIterator
import freemarker.template.TemplateModel.NOTHING
import jakarta.mail.internet.InternetAddress
import java.util.*








private const val SUBJECT_KEY = "subject"
private const val FROM_KEY = "from"
private const val REPLY_TO_KEY = "replyTo"
private const val TO_KEY = "to"

private val FUNCTION_KEYS = setOf(SUBJECT_KEY, FROM_KEY, REPLY_TO_KEY, TO_KEY)

class MailModelAndView(val templateName: String) : TemplateHashModelEx2 {

    private val wrapper = DefaultObjectWrapperBuilder(Version("2.3.23")).build()

    // values written from the model side
    private val delegate: HashMap<String, TemplateModel> = HashMap()

    var locale: Locale = Locale.ENGLISH

    var mailSubject: String? = null

    var mailFrom: String? = null

    var mailReplyTo: String? = null

    val mailTo: MutableList<InternetAddress> = mutableListOf()


    init {
        // adding functions to the model that can be called inside the template
        // each key-value is a custom method (see: https://www.baeldung.com/freemarker-operations) implementing the exec method
        // and can be used inside the template to execute a side effect (storing a value in the model for reading later)
        // we return a nothing value that will be inserted in the template

        delegate[SUBJECT_KEY] = TemplateMethodModelEx { arguments: List<*> ->
            require(arguments.size == 1) { "only one element allowed for call to $SUBJECT_KEY" }
            mailSubject = arguments[0].toString()
            NOTHING // render nothing, the call is used to set a value in the template model as a side effect
        }

        delegate[FROM_KEY] = TemplateMethodModelEx { arguments: List<*> ->
            require(arguments.size == 1) { "only one element allowed for call to $FROM_KEY" }
            mailFrom = arguments[0].toString()
            NOTHING
        }

        delegate[REPLY_TO_KEY] = TemplateMethodModelEx { arguments: List<*> ->
            require(arguments.size == 1) { "only one element allowed for call to $REPLY_TO_KEY" }
            mailReplyTo = arguments[0].toString()
            NOTHING
        }

        delegate[TO_KEY] = TemplateMethodModelEx { arguments: List<*> ->
            require(arguments.isNotEmpty()) { "only one element allowed for call to 'replyTo()'" }
            mailTo.addAll(arguments.toTypedArray().filterNotNull().map { argument -> InternetAddress(argument.toString()) })
            NOTHING
        }
    }

    override operator fun get(key: String): TemplateModel {
        return delegate[key] ?: NOTHING
    }

    override fun isEmpty(): Boolean {
        return delegate.isEmpty()
    }

    override fun size(): Int {
        return delegate.size
    }

    override fun keys(): TemplateCollectionModel {
        return CollectionAndSequence(SimpleSequence(delegate.keys, wrapper))
    }

    override fun values(): TemplateCollectionModel {
        return CollectionAndSequence(SimpleSequence(delegate.values, wrapper))
    }

    override fun keyValuePairIterator(): KeyValuePairIterator {
        return MapKeyValuePairIterator(delegate, wrapper)
    }

    fun put(key: String, value: Any) {
        try {
            require(key !in FUNCTION_KEYS) { "key $key is reserved for a function and can not be used" }
            delegate[key] = wrapper.wrap(value)
        } catch (ex: TemplateModelException) {
            throw MailException("error wrapping object for freemarker template", ex)
        }
    }
}

class MapKeyValuePairIterator(map: HashMap<String, TemplateModel>, private val objectWrapper: ObjectWrapper) :
    KeyValuePairIterator {
    private val entrySetIterator: Iterator<Map.Entry<String, TemplateModel>>

    init {
        entrySetIterator = map.entries.iterator()
    }

    override fun hasNext(): Boolean {
        return entrySetIterator.hasNext()
    }

    override fun next(): TemplateHashModelEx2.KeyValuePair {
        val (key, value) = entrySetIterator.next()
        return object : TemplateHashModelEx2.KeyValuePair {
            @Throws(TemplateModelException::class)
            override fun getKey(): TemplateModel {
                return wrap(key)
            }

            @Throws(TemplateModelException::class)
            override fun getValue(): TemplateModel {
                return wrap(value)
            }
        }
    }

    @Throws(TemplateModelException::class)
    private fun wrap(obj: Any): TemplateModel {
        return if (obj is TemplateModel) obj else objectWrapper.wrap(obj)
    }
}


package net.wohlfart.charon.entity

import jakarta.persistence.*
import org.hibernate.annotations.ValueGenerationType
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.generator.BeforeExecutionGenerator
import org.hibernate.generator.EventType
import org.hibernate.generator.Generator
import java.util.*


val EVENTS = EnumSet.of(EventType.INSERT)!!

const val SHORT_SEQ_SIZE = 6


class ShortSequenceGenerator : Generator, BeforeExecutionGenerator {

    override fun generatedOnExecution(): Boolean {
        return false
    }

    override fun getEventTypes(): EnumSet<EventType> {
        return EVENTS
    }

    override fun generate(session: SharedSessionContractImplementor, owner: Any?, currentValue: Any?, eventType: EventType?): String {
        var currentId = session.createNamedSelectionQuery(FETCH_QUERY, Int::class.java).uniqueResult()
        if (currentId == null) {
            currentId = 1_000_000
            session.createNamedMutationQuery(INSERT_QUERY).setParameter("next_value", currentId).executeUpdate()
        }
        session.createNamedMutationQuery(UPDATE_QUERY).setParameter("next_value", (currentId + 1)).executeUpdate()
        return Integer.toString(currentId, 36).padStart(SHORT_SEQ_SIZE, '0') // uses 0123456789abcdefghijklmnopqrstuvwxyz as digits
        // lfls
        // l = (9 + 12) * 36 ^ 3  = 979776
        // f = (9 +  6) * 36 ^ 2  =  19440
        // l = (9 + 12) * 36 ^ 1  =    756
        // s = (9 + 19) * 36 ^ 0  =     28   = 1_000_000
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@ValueGenerationType(generatedBy = ShortSequenceGenerator::class)
annotation class ExternalIdSequence
const val EXTERNAL_ID_SEQUENCE = "external-id-sequence"


const val FETCH_QUERY = "FETCH_QUERY"
const val INSERT_QUERY = "INSERT_QUERY"
const val UPDATE_QUERY = "UPDATE_QUERY"

@NamedQueries(
    NamedQuery(
        name = INSERT_QUERY,
        query = "INSERT INTO UserSequence (sequenceName, nextValue) VALUES ('${EXTERNAL_ID_SEQUENCE}', :next_value)"
    ),
    NamedQuery(
        name = FETCH_QUERY,
        query = "SELECT nextValue from UserSequence where sequenceName = '${EXTERNAL_ID_SEQUENCE}'"
    ),
    NamedQuery(
        name = UPDATE_QUERY,
        query = "UPDATE UserSequence set nextValue = :next_value where sequenceName = '${EXTERNAL_ID_SEQUENCE}'"
    )
)
@Entity
@Table(name = "user_sequence_generator")
data class UserSequence(

    @Id
    @Column(name = "sequence_name", unique = true, nullable = false, length = 64)
    private var sequenceName: String? = null,

    @Column(name = "next_value", unique = false, nullable = false)
    private var nextValue: Int? = null,

    )

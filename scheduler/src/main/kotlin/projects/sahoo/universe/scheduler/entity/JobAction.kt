package projects.sahoo.universe.scheduler.entity

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import javax.persistence.AttributeConverter
import javax.persistence.Converter

interface JobAction {
    val type: JobActionType
}

data class ApiCallAction(
    override val type: JobActionType = JobActionType.API_CALL,
    val method: HttpMethod,
    val host: String,
    val path: String? = null,
    val requestBody: String? = null
) : JobAction

enum class JobActionType {
    API_CALL
}

@Converter
class JobActionConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<JobAction, String> {
    override fun convertToDatabaseColumn(jobAction: JobAction?): String? =
        jobAction?.let {
            objectMapper.writeValueAsString(it)
        }

    override fun convertToEntityAttribute(jobActionString: String?): JobAction? =
        jobActionString?.let {
            val json = objectMapper.readTree(jobActionString)
            when (json.get("type").textValue()) {
                JobActionType.API_CALL.name -> objectMapper.readValue(jobActionString, ApiCallAction::class.java)
                else -> throw IllegalArgumentException("Invalid job action: $jobActionString")
            }
        }
}

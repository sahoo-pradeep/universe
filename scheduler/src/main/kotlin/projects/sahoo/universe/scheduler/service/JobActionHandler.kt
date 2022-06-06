package projects.sahoo.universe.scheduler.service

import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import projects.sahoo.universe.scheduler.entity.ApiCallAction
import projects.sahoo.universe.scheduler.entity.JobAction
import projects.sahoo.universe.scheduler.entity.JobActionType
import kotlin.random.Random

@Component
class JobActionHandlerFactory(
    private val apiCallJobActionHandler: ApiCallJobActionHandler
) {
    fun getHandler(actionType: JobActionType): JobActionHandler =
        when (actionType) {
            JobActionType.API_CALL -> apiCallJobActionHandler
        }
}

interface JobActionHandler {
    fun execute(jobAction: JobAction)
}

@Component
class ApiCallJobActionHandler(
    private val okHttpClient: OkHttpClient
) : JobActionHandler {
    val logger = KotlinLogging.logger { }

    override fun execute(jobAction: JobAction) {
        if (jobAction.type != JobActionType.API_CALL) {
            throw RuntimeException("Invalid job action: $jobAction")
        }
        jobAction as ApiCallAction

        if (jobAction.method != HttpMethod.GET) {
            throw RuntimeException("Only GET http method is supported in ApiCallJobAction")
        }

        val request = Request.Builder()
            .url(jobAction.host + if (jobAction.path.isNullOrBlank()) "" else "/${jobAction.path}")
            .build()

        logger.info { "api request: $request" }
        val response = okHttpClient.newCall(request).execute()
        Thread.sleep(Random.nextLong(2_000, 4_000)) // To delay response
        logger.info { "api response: $response" }
    }
}

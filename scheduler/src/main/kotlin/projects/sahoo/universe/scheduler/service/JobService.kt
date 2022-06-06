package projects.sahoo.universe.scheduler.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import projects.sahoo.universe.scheduler.entity.Job
import projects.sahoo.universe.scheduler.repository.JobRepository
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class JobService(
    private val jobActionHandlerFactory: JobActionHandlerFactory,
    private val jobRepository: JobRepository
) {
    private val logger = KotlinLogging.logger {}
    fun getAllActiveJobs(): List<Job> {
        // TODO: add other checks
        return jobRepository.findAll()
    }

    fun executeJob(job: Job) {
        val startTime = System.currentTimeMillis()
        logger.info { "Starting job: ${job.name}" }
        val handler = jobActionHandlerFactory.getHandler(job.action.type)
        handler.execute(job.action)
        val duration = (System.currentTimeMillis() - startTime).toDuration(DurationUnit.MILLISECONDS)
        logger.info { "Job ${job.name} completed in $duration" }
    }
}

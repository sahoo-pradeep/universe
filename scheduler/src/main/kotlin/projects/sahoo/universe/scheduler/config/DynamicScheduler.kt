package projects.sahoo.universe.scheduler.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.support.CronTrigger
import projects.sahoo.universe.scheduler.entity.Job
import projects.sahoo.universe.scheduler.service.JobService
import java.util.concurrent.ScheduledFuture

/**
 * TODO: Description about this class
 * @see <a href="https://www.netiq.com/documentation/cloud-manager-2-5/ncm-reference/data/bexyssf.html">Understanding Cron</a>
 */
@Configuration
class DynamicScheduler(
    private val jobService: JobService
) : SchedulingConfigurer {

    private val logger = KotlinLogging.logger { }
    private val scheduledJobs = mutableMapOf<String, ScheduledFuture<*>>()
    private var scheduledTaskRegistrar: ScheduledTaskRegistrar? = null

    @Bean
    fun poolScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler")
        scheduler.initialize()
        return scheduler
    }

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        scheduledTaskRegistrar = scheduledTaskRegistrar ?: taskRegistrar
        taskRegistrar.scheduler ?: taskRegistrar.setScheduler(poolScheduler())

        initJobs()
    }

    private fun initJobs() {
        logger.info { "Initializing Jobs" }
        val jobs = jobService.getAllActiveJobs()
        logger.info { "Found ${jobs.size} active jobs" }
        jobs.forEach {
            scheduledTaskRegistrar?.scheduler?.schedule({ jobService.executeJob(it) }, CronTrigger(it.cron))!!
                .let { future -> scheduledJobs[it.name] = future }
        }
    }

    fun addJob(job: Job): Boolean {
        if (scheduledJobs[job.name] != null) {
            logger.info { "Job ${job.name} is already added. Ignoring add job request" }
            return false
        }

        scheduledTaskRegistrar?.scheduler?.schedule({ jobService.executeJob(job) }, CronTrigger(job.cron))!!
            .let { future -> scheduledJobs[job.name] = future }
        return true
    }

    fun removeJob(job: Job): Boolean {
        if (scheduledJobs[job.name] == null) {
            logger.info { "Job ${job.name} doesn't exist. Ignoring remove job request" }
            return false
        }

        scheduledJobs[job.name]!!.cancel(true)
        scheduledJobs.remove(job.name)
        return true
    }

    fun updateJob(job: Job): Boolean =
        removeJob(job) && addJob(job)
}

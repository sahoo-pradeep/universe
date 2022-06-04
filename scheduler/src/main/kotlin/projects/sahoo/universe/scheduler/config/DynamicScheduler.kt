package projects.sahoo.universe.scheduler.config

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

    // TODO: logging

    private var scheduledTaskRegistrar: ScheduledTaskRegistrar? = null
    private val scheduledJobs = mutableMapOf<String, ScheduledFuture<*>>()

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

    private fun initJobs() =
        jobService.getAllActiveJobs().forEach {
            scheduledTaskRegistrar?.scheduler?.schedule({ jobService.executeJob(it) }, CronTrigger(it.cron))!!
                .let { future -> scheduledJobs[it.name] = future }
        }

    fun addJob(job: Job) {
        TODO("NOT YET IMPLEMENTED")
    }

    fun removeJob(job: Job) {
        TODO("NOT YET IMPLEMENTED")
    }
}

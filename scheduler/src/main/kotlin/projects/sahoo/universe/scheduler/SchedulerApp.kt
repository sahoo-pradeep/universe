package projects.sahoo.universe.scheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SchedulerApp

fun main(args: Array<String>) {
    runApplication<SchedulerApp>(*args)
}

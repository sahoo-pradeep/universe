package projects.sahoo.universe.scheduler.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Job(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val uuid: String,
    val name: String,
    val version: Short,
    val cron: String,
    @Convert(converter = JobActionConverter::class)
    val action: JobAction,
    val active: Boolean,
    @CreationTimestamp
    val createdAt: LocalDateTime
)

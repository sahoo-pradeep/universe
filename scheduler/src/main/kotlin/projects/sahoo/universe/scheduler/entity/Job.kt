package projects.sahoo.universe.scheduler.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Job(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val name: String,
    val version: Short,
//    @Enumerated(EnumType.STRING)
//    val type: JobType,
//    val action: String,
    val cron: String,
//    val active: Boolean,
    @CreationTimestamp
    val createdAt: LocalDateTime
)

enum class JobType {
    API_CALL
}

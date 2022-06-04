package projects.sahoo.universe.scheduler.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import projects.sahoo.universe.scheduler.entity.Job

@Repository
interface JobRepository : JpaRepository<Job, Int>

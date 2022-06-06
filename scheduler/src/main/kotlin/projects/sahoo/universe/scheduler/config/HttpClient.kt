package projects.sahoo.universe.scheduler.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClient {
    @Bean
    fun okHttpClient(): OkHttpClient =
        OkHttpClient()
}

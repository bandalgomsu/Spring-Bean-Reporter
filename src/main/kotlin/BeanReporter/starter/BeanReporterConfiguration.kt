package BeanReporter.starter

import BeanReporter.core.BeanReporter
import BeanReporter.core.BeanReporterRunner
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BeanReportProperties::class)
class BeanReporterConfiguration(
    private val context: ApplicationContext,
    private val beanInitializationTimer: BeanInitializationTimer,
    private val beanReportProperties: BeanReportProperties,
) {

    @Bean
    fun beanReporter(): BeanReporter {
        return BeanReporter(context, beanInitializationTimer, beanReportProperties)
    }

    @Bean
    fun beanSentryRunner(): ApplicationRunner {
        return BeanReporterRunner(beanReporter())
    }
}
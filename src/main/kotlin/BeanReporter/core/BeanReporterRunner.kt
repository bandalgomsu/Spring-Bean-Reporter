package BeanReporter.core

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner

class BeanReporterRunner(
    private val beanReporter: BeanReporter
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        beanReporter.reportBeans()
    }
}
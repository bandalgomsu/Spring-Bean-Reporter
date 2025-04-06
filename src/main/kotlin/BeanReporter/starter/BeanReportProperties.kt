package BeanReporter.starter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bean.report")
class BeanReportProperties {
    var initThresholdMs: Long = 100
    var fatBeanDependencyThreshold: Int = 6
    var includeBasePackages: MutableList<String> = mutableListOf()
}

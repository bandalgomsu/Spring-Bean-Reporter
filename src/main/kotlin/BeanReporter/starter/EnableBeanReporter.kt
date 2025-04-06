package BeanReporter.starter

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(BeanReporterConfiguration::class)
annotation class EnableBeanReporter
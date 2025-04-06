package BeanReporter.core

import BeanReporter.starter.BeanInitializationTimer
import BeanReporter.starter.BeanReportProperties
import org.springframework.context.ApplicationContext

class BeanReporter(
    private val context: ApplicationContext,
    private val timer: BeanInitializationTimer,
    private val properties: BeanReportProperties
) {
    fun reportBeans() {
        println("🚀 Bean Reporter 실행 시작")

        println("\n🧠 Spring Bean 구조 분석 결과")

        val graph = BeanAnalyzer().buildGraph(context)
        val printer = BeanReportPrinter()

        printer.printFatBeans(graph, properties.fatBeanDependencyThreshold)
        printer.printUnusedBeans(graph, properties.includeBasePackages)
        printer.printCircularBean(graph)
        printer.printSlowBeans(timer, thresholdMs = properties.initThresholdMs)
    }
}
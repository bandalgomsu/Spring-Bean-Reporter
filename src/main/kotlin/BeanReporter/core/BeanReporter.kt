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
        println("🚀 Bean Reporter Start !!")

        println("\n🧠 Spring Bean Structure Analysis Results")

        val graph = BeanAnalyzer().buildGraph(context)
        val printer = BeanReportPrinter()

        printer.printFatBeans(graph, properties.fatBeanDependencyThreshold, properties.includeBasePackages)
        printer.printUnusedBeans(graph, properties.includeBasePackages)
        printer.printCircularBean(graph, properties.includeBasePackages)
        printer.printRootBeansDependencyTree(graph, properties.dependencyTreeTargetBeansName)
        printer.printSlowBeans(timer, thresholdMs = properties.initThresholdMs)
    }
}
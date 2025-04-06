package BeanReporter.core

import BeanReporter.starter.BeanInitializationTimer
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

class BeanReportPrinter {
    fun printFatBeans(graph: BeanGraph, fatBeanThreshold: Int = 6) {
        println("\n📦 Fat Beans (dependencies ≥ ${fatBeanThreshold}):")
        graph.findFatBeans(fatBeanThreshold).forEach {
            println(" - ${it.name} (${it.dependencies.size}): ${it.dependencies.joinToString()}")
        }
    }

    fun printUnusedBeans(graph: BeanGraph, includePackages: List<String>) {

        val unused = graph.findUnusedBeans()

        val (exclusion, external) = unused.partition {
            !isIncludePackage(it.type, includePackages)
            isSpringFrameworkEntryPointBean(it.type)
        }

        println("\n📭 Unused Beans (possibly dead code):")
        if (external.isEmpty()) println(" - None")
        else external.forEach {
            println(" - ${it.name} (${it.packageName}.${it.type.simpleName})")
        }

//        println("\n📄 Internal/Framework/Configuration/Schedule Beans (ignored in usage analysis):")
//        if (exclusion.isEmpty()) println(" - None")
//        else exclusion.forEach {
//            println(" - ${it.name} (${it.packageName}.${it.type.simpleName}) [@${it.type.annotations.joinToString { a -> a.annotationClass.simpleName ?: "?" }}]")
//        }
    }

    private fun isIncludePackage(type: Class<*>, includePackages: List<String>): Boolean {
        val pkg = type.`package`?.name ?: return false
        return includePackages.any { pkg.startsWith(it) }
    }

    private fun isSpringFrameworkEntryPointBean(type: Class<*>): Boolean {
        val rawType = type.takeIf { !it.name.contains("CGLIB") } ?: type.superclass


        return ApplicationRunner::class.java.isAssignableFrom(type)
                || CommandLineRunner::class.java.isAssignableFrom(type)
                || SmartInitializingSingleton::class.java.isAssignableFrom(type)
                || BeanFactoryPostProcessor::class.java.isAssignableFrom(type)
                || rawType?.isAnnotationPresent(Configuration::class.java) == true
                || rawType?.isAnnotationPresent(Scheduled::class.java) == true
                || rawType?.isAnnotationPresent(SpringBootApplication::class.java) == true
                || rawType?.isAnnotationPresent(Controller::class.java) == true
                || rawType?.isAnnotationPresent(EventListener::class.java) == true
                || rawType?.name?.startsWith("org.springframework.") == true
                || rawType?.name?.contains("springframework") == true
                || rawType?.methods?.any { it.isAnnotationPresent(Scheduled::class.java) } == true
                || rawType?.name?.contains("mbean", ignoreCase = true) == true

    }

    fun printCircularBean(graph: BeanGraph) {
        println("\n♻️ Circular Dependencies:")
        graph.findCircularDependencies().forEach {
            println(" - ${it.joinToString(" -> ")}")
        }
    }

    fun printSlowBeans(timer: BeanInitializationTimer, thresholdMs: Long = 100) {
        println("\n🐢 Slow Bean Initializations (>${thresholdMs}ms):")
        val slowBeans = timer.getInitializationDurations()
            .filter { it.value >= thresholdMs }
            .toList()
            .sortedByDescending { it.second }

        if (slowBeans.isEmpty()) {
            println(" - None")
        } else {
            slowBeans.forEach { (name, ms) ->
                println(" - $name : ${ms}ms")
            }
        }
    }
}
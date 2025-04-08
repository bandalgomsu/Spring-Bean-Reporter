package BeanReporter.core

import BeanReporter.starter.BeanInitializationTimer
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

class BeanReportPrinter {
    fun printFatBeans(graph: BeanGraph, fatBeanThreshold: Int = 6, includePackages: List<String>) {
        println("\n📦 Fat Beans (dependencies ≥ ${fatBeanThreshold}):")

        val fatBean = graph.findFatBeans(fatBeanThreshold)

        val (target, external) = fatBean.partition { isIncludePackage(it.type, includePackages) }

        target.forEach {
            println(" - ${it.name} (${it.dependencies.size}): ${it.dependencies.joinToString()}")
        }
    }

    fun printUnusedBeans(graph: BeanGraph, includePackages: List<String>) {

        val unused = graph.findUnusedBeans()

        val (external, target) = unused.partition {
            !isIncludePackage(it.type, includePackages) || isExternal(it.type)
        }

        println("\n📭 Unused Beans (possibly dead code):")
        if (target.isEmpty()) println(" - None")
        else target.forEach {
            println(" - ${it.name} (${it.packageName}.${it.type.simpleName})")
        }

//        println("\n📄 Internal/Framework/Configuration/Schedule Beans (ignored in usage analysis):")
//        if (exclusion.isEmpty()) println(" - None")
//        else exclusion.forEach {
//            println(" - ${it.name} (${it.packageName}.${it.type.simpleName}) [@${it.type.annotations.joinToString { a -> a.annotationClass.simpleName ?: "?" }}]")
//        }
    }

    fun printCircularBean(graph: BeanGraph, includePackages: List<String>) {
        println("\n♻️ Circular Dependencies:")
        val circularBean = graph.findCircularDependencies()

        circularBean.forEach {
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

    private fun isIncludePackage(type: Class<*>, includePackages: List<String>): Boolean {
        val pkg = type.`package`?.name ?: return false
        return includePackages.any { pkg.startsWith(it) }
    }

    private fun isExternal(type: Class<*>): Boolean {
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
                || rawType?.isAnnotationPresent(RestController::class.java) == true
                || rawType?.isAnnotationPresent(RestControllerAdvice::class.java) == true
                || rawType?.isAnnotationPresent(ControllerAdvice::class.java) == true
                || rawType?.name?.startsWith("org.springframework.") == true
                || rawType?.name?.contains("springframework") == true
                || rawType?.name?.contains("springdoc") == true
                || rawType?.methods?.any { it.isAnnotationPresent(Scheduled::class.java) } == true
                || rawType?.methods?.any { it.isAnnotationPresent(PostConstruct::class.java) } == true
                || rawType?.name?.contains("mbean", ignoreCase = true) == true
    }
}
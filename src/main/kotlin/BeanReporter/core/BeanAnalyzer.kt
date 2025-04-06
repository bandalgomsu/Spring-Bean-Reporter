package BeanReporter.core

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.context.ApplicationContext

class BeanAnalyzer {
    fun buildGraph(context: ApplicationContext): BeanGraph {
        val factory = context.autowireCapableBeanFactory as DefaultListableBeanFactory
        val graph = BeanGraph()

        factory.beanDefinitionNames.forEach { beanName ->
            val type = context.getType(beanName) ?: return@forEach
            val ctor = type.declaredConstructors.firstOrNull()
            val deps = ctor?.parameters?.mapNotNull { param ->
                val qualifier = param.getAnnotation(Qualifier::class.java)?.value
                qualifier ?: factory.getBeanNamesForType(param.type).firstOrNull()
            } ?: emptyList()
            
            val packageName = type.packageName

            graph.add(BeanInfo(beanName, type, deps, packageName))
        }

        return graph
    }
}

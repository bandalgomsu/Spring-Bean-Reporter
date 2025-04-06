package BeanReporter.starter

import  org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class BeanInitializationTimer : BeanPostProcessor {

    private val startTimes = ConcurrentHashMap<String, Long>()
    private val durations = ConcurrentHashMap<String, Long>()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        startTimes[beanName] = System.nanoTime()
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        startTimes[beanName]?.let { start ->
            val duration = System.nanoTime() - start
            durations[beanName] = duration
        }
        return bean
    }

    fun getInitializationDurations(): Map<String, Long> {
        return durations.mapValues { it.value / 1_000_000 } // ms 단위로 변환
    }
}

package BeanReporter.core

data class BeanInfo(
    val name: String,
    val type: Class<*>,
    val dependencies: List<String>,
    val packageName: String
)

package BeanReporter.core

class BeanGraph(
    private val nodes: MutableMap<String, BeanInfo> = mutableMapOf<String, BeanInfo>()
) {
    fun add(bean: BeanInfo) {
        nodes[bean.name] = bean
    }

    fun findUnusedBeans(): List<BeanInfo> {
        val used = nodes.values.flatMap { it.dependencies }.toSet()
        return nodes.values.filter { it.name !in used }
    }

    fun findFatBeans(threshold: Int = 6): List<BeanInfo> {
        return nodes.values.filter { it.dependencies.size >= threshold }
    }

    fun findCircularDependencies(): List<List<String>> {
        val visited = mutableSetOf<String>()
        val stack = mutableListOf<String>()
        val result = mutableListOf<List<String>>()

        fun dfs(bean: String) {
            if (bean in stack) {
                val cycle = stack.dropWhile { it != bean } + bean
                result.add(cycle)
                return
            }
            if (bean in visited) return
            visited += bean
            stack += bean
            nodes[bean]?.dependencies?.forEach { dfs(it) }
            stack.removeLast()
        }

        nodes.keys.forEach { dfs(it) }
        return result.distinct()
    }
}
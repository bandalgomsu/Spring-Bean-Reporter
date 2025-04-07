package BeanReporter.core

class BeanGraph(
    private val nodes: MutableMap<String, BeanInfo> = mutableMapOf()
) {
    fun add(bean: BeanInfo) {
        nodes[bean.name] = bean
    }

    fun findUnusedBeans(): List<BeanInfo> {
        val used = nodes.values.flatMap { it.dependencies }.toSet()

        return nodes.values.filter { node ->
            used.none { usedName ->
                usedName == node.name || usedName.startsWith("${node.name}$$")
            }
        }
    }

    fun findFatBeans(threshold: Int = 6): List<BeanInfo> {
        return nodes.values.filter { it.dependencies.size >= threshold }
    }

    fun findCircularDependencies(): List<List<String>> {
        val visited = mutableSetOf<String>()
        val stack = mutableListOf<String>()
        val result = mutableSetOf<List<String>>() // 중복 방지

        fun dfs(bean: String) {
            if (bean in stack) {
                val cycle = stack.dropWhile { it != bean } + bean

                // 🔥 오탐 방지: 자기 자신만 참조하는 순환은 제외
                if (cycle.toSet().size > 1) {
                    result += cycle
                }
                return
            }

            if (bean in visited) return
            visited += bean
            stack += bean
            nodes[bean]?.dependencies?.forEach { dfs(it) }
            stack.removeLast()
        }

        nodes.keys.forEach { dfs(it) }
        return result.toList()
    }
}
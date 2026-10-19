// port-lint: source kinda_sorta_dangling.rs
package io.github.kotlinmania.yoke

typealias Target = Any

class KindaSortaDangling<T>(
    private var dangle: T,
) {
    companion object {
        fun <T> new(dangle: T): KindaSortaDangling<T> = KindaSortaDangling(dangle)
    }

    fun new(dangle: T): KindaSortaDangling<T> = KindaSortaDangling(dangle)

    fun intoInner(): T = dangle

    fun deref(): T = dangle

    fun derefMut(): T = dangle

    fun drop() {
    }
}

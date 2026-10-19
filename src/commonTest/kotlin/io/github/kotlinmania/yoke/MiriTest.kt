// port-lint: tests miri.rs
package io.github.kotlinmania.yoke

import kotlin.test.Test
import kotlin.test.assertEquals

class MiriTest {
    private fun example(yoke: Yoke<ByteArray, ByteArray>) {
        assertEquals(3, yoke.get().size)
    }

    @Test
    fun runTest() {
        example(Yoke.attachToCart(byteArrayOf(0, 1, 2)) { data -> data })
    }
}

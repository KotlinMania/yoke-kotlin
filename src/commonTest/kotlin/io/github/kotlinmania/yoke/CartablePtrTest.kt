package io.github.kotlinmania.yoke

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CartablePtrTest {
    @Test
    fun testSizes() {
        assertEquals(W * 4, 32)
    }

    @Test
    fun testNewSentinel() {
        val ptr = CartableOptionPointer.none<BoxPointer<ByteArray>>()
        assertTrue(ptr.isNone())
    }

    @Test
    fun testNewRc() {
        val ptr = CartableOptionPointer.fromCartable(RcPointer(SAMPLE_BYTES))
        assertTrue(!ptr.isNone())
    }

    @Test
    fun testRcClone() {
        val ptr = CartableOptionPointer.fromCartable(RcPointer(SAMPLE_BYTES))
        val cloned = ptr.clone()
        assertTrue(!cloned.isNone())
    }
}

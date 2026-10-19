// port-lint: tests bincode.rs
package io.github.kotlinmania.yoke

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Bar(
    var numbers: Cow<ByteArray>,
    var string: Cow<String>,
    var owned: MutableList<Byte> = mutableListOf(),
) : Yokeable<Bar> {
    typealias Output = Bar

    override fun transform(): Bar = this

    override fun transformOwned(): Bar = this

    override fun make(from: Bar): Bar = from

    override fun transformMut(f: (Bar) -> Unit) {
        f(this)
    }
}

class BincodeTest {
    private fun loadFromCache(filename: String): ByteArray {
        check(filename.isNotEmpty())
        val dummy = byteArrayOf(0x5, 0, 0, 0, 0, 0, 0, 0, 0x68, 0x65, 0x6c, 0x6c, 0x6f)
        return dummy
    }

    private fun loadObject(filename: String): Yoke<Bar, ByteArray> {
        val rc = loadFromCache(filename)
        return Yoke.attachToCart(rc) { data ->
            val strBytes = data.sliceArray(8 until data.size)
            val str = strBytes.decodeToString()
            Bar(
                numbers = Cow.Borrowed(strBytes),
                string = Cow.Borrowed(str),
                owned = mutableListOf(),
            )
        }
    }

    @Test
    fun testLoad() {
        val bar = loadObject("filename.bincode")
        assertEquals("hello", bar.get().string.value)
        assertTrue(bar.get().string is Cow.Borrowed)
        val expectedNumbers = byteArrayOf(0x68, 0x65, 0x6c, 0x6c, 0x6f)
        assertTrue(
            bar
                .get()
                .numbers.value
                .contentEquals(expectedNumbers),
        )
        assertTrue(bar.get().numbers is Cow.Borrowed)
        assertTrue(bar.get().owned.isEmpty())

        bar.withMut { b ->
            b.string = Cow.Owned(b.string.value + " world")
            b.owned.addAll(listOf(1, 4, 1, 5, 9).map { it.toByte() })
        }

        assertEquals("hello world", bar.get().string.value)
        assertTrue(bar.get().string is Cow.Owned)
        val expectedOwned = listOf<Byte>(1, 4, 1, 5, 9)
        assertEquals(expectedOwned, bar.get().owned)
        assertTrue(
            bar
                .get()
                .numbers.value
                .contentEquals(expectedNumbers),
        )
        assertTrue(bar.get().numbers is Cow.Borrowed)
    }
}

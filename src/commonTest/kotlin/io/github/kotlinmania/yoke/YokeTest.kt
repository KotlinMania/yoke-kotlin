package io.github.kotlinmania.yoke

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class CowStringMutator(
    private val cow: Cow<String>,
) {
    fun pushStr(s: String) {
        cow.value = cow.value + s
    }
}

private fun Cow<String>.toMut(): CowStringMutator = CowStringMutator(this)

class YokeTest {
    @Test
    fun testDebug() {
        val localData = "foo"
        val y1 = Yoke.attachToZeroCopyCart(localData)
        assertEquals(
            y1.fmt(),
            "Yoke { yokeable: \"foo\", cart: \"foo\" }",
        )
    }

    @Test
    fun testClone() {
        val localData = "foo"
        val y1 = Yoke.attachToZeroCopyCart(localData)

        val y2 = y1.clone()
        assertEquals("foo", y1.get().value)
        assertEquals("foo", y2.get().value)

        val y3 = y1.clone()
        y3.withMut { y ->
            y.toMut().pushStr("bar")
        }
        assertEquals("foo", y1.get().value)
        assertEquals("foo", y2.get().value)
        assertEquals("foobar", y3.get().value)

        val y4 = y3.clone()
        y3.withMut { y ->
            y.toMut().pushStr("baz")
        }
        assertEquals("foo", y1.get().value)
        assertEquals("foo", y2.get().value)
        assertEquals("foobarbaz", y3.get().value)
        assertEquals("foobar", y4.get().value)
    }

    @Test
    fun testAttachToCart() {
        val cart = "hello world"
        val yoke = Yoke.attachToCart(cart) { it.substring(0, 5) }
        assertEquals("hello", yoke.get())
        assertEquals("hello world", yoke.backingCart())
        assertEquals("hello world", yoke.intoBackingCart())
    }

    @Test
    fun testMapProject() {
        val yoke = Yoke.attachToCart("hello") { it }
        val projected = yoke.mapProject { text, _ -> text.length }
        assertEquals(5, projected.get())
    }

    @Test
    fun testMapWithCart() {
        val yoke = Yoke.attachToCart("hello\nworld") { it.lines().first() }
        assertEquals("hello", yoke.get())
        val mapped = yoke.mapWithCart { first, cart -> "$first ${cart.lines().last()}" }
        assertEquals("hello world", mapped.get())
    }

    @Test
    fun testNewOwnedAndOptions() {
        val yoke = Yoke.newOwned<String, String>("owned")
        assertEquals("owned", yoke.get())
        assertEquals("owned", yoke.tryIntoYokeable())

        val alwaysOwned = Yoke.newAlwaysOwned("always")
        assertEquals("always", alwaysOwned.get())
        assertEquals("always", alwaysOwned.intoYokeable())
    }

    @Test
    fun testEitherCart() {
        val y1 = Yoke.attachToCart("cartA") { it }
        val wrappedA = y1.wrapCartInEitherA<String>()
        assertTrue(wrappedA.backingCart() is EitherCart.A)

        val y2 = Yoke.attachToCart("cartB") { it }
        val wrappedB = y2.wrapCartInEitherB<String>()
        assertTrue(wrappedB.backingCart() is EitherCart.B)
    }

    @Test
    fun testEraseCart() {
        val yoke = Yoke.attachToCart("data") { it }
        val erasedRc = yoke.eraseRcCart()
        assertEquals("data", erasedRc.get())

        val erasedArc = yoke.eraseArcCart()
        assertEquals("data", erasedArc.get())

        val erasedBox = yoke.eraseBoxCart()
        assertEquals("data", erasedBox.get())
    }
}

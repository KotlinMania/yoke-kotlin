// port-lint: source zero_from.rs
package io.github.kotlinmania.yoke

interface ZeroFrom<T, C> {
    fun zeroFrom(cart: C): T
}

fun <T> attachToZeroCopyCart(cart: T): Yoke<Cow<T>, T> = Yoke.attachToZeroCopyCart(cart)

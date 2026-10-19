// port-lint: source cartable_ptr.rs
package io.github.kotlinmania.yoke

interface Sealed

val SAMPLE_BYTES: ByteArray = byteArrayOf(97, 98, 67, 68, 69, 102, 103)
const val W: Int = 8

typealias Raw = Any

private val sentinelObject = Any()

fun <T> sentinelFor(): Any = sentinelObject

interface CartablePointerLike : Sealed {
    fun intoRaw(): Any

    fun dropRaw(pointer: Any)
}

interface CloneableCartablePointerLike : CartablePointerLike {
    fun addrefRaw(pointer: Any)
}

class BoxPointer<T>(
    private val value: T,
) : CartablePointerLike {
    override fun intoRaw(): Any = value as Any

    override fun dropRaw(pointer: Any) {
        checkNotNull(pointer)
    }
}

class RcPointer<T>(
    private val value: T,
) : CloneableCartablePointerLike {
    override fun intoRaw(): Any = value as Any

    override fun dropRaw(pointer: Any) {
        checkNotNull(pointer)
    }

    override fun addrefRaw(pointer: Any) {
        checkNotNull(pointer)
    }
}

class ArcPointer<T>(
    private val value: T,
) : CloneableCartablePointerLike {
    override fun intoRaw(): Any = value as Any

    override fun dropRaw(pointer: Any) {
        checkNotNull(pointer)
    }

    override fun addrefRaw(pointer: Any) {
        checkNotNull(pointer)
    }
}

class CartableOptionPointer<C : CartablePointerLike>(
    private var inner: Any?,
) : CloneableCart {
    companion object {
        fun <C : CartablePointerLike> none(): CartableOptionPointer<C> = CartableOptionPointer(sentinelObject)

        fun <C : CartablePointerLike> fromCartable(cartable: C): CartableOptionPointer<C> = CartableOptionPointer(cartable.intoRaw())
    }

    fun isNone(): Boolean = inner === sentinelObject || inner == null

    fun drop() {
        if (!isNone()) {
            inner = sentinelObject
        }
    }

    fun clone(): CartableOptionPointer<C> = CartableOptionPointer(inner)

    override fun toString(): String = if (isNone()) "None" else "Some($inner)"
}

// port-lint: source yokeable.rs
package io.github.kotlinmania.yoke

typealias Output = Any

interface Yokeable<Output> {
    fun transform(): Output

    fun transformOwned(): Output

    fun make(from: Output): Yokeable<Output>

    fun transformMut(f: (Output) -> Unit)
}

sealed class Cow<T> : Yokeable<Cow<T>> {
    abstract var value: T

    class Borrowed<T>(
        override var value: T,
    ) : Cow<T>()

    class Owned<T>(
        override var value: T,
    ) : Cow<T>()

    override fun transform(): Cow<T> = this

    override fun transformOwned(): Cow<T> = this

    override fun make(from: Cow<T>): Cow<T> = from

    override fun transformMut(f: (Cow<T>) -> Unit) {
        f(this)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Cow<*>) return value == other.value
        return value == other
    }

    override fun hashCode(): Int = value?.hashCode() ?: 0

    override fun toString(): String = value.toString()
}

class PhantomData<T> : Yokeable<PhantomData<T>> {
    override fun transform(): PhantomData<T> = this

    override fun transformOwned(): PhantomData<T> = this

    override fun make(from: PhantomData<T>): PhantomData<T> = from

    override fun transformMut(f: (PhantomData<T>) -> Unit) {
        f(this)
    }
}

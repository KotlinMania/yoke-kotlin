// port-lint: source either.rs
package io.github.kotlinmania.yoke

sealed class EitherCart<C0, C1> : CloneableCart {
    typealias Target = Any?

    data class A<C0, C1>(
        val value: C0,
    ) : EitherCart<C0, C1>()

    data class B<C0, C1>(
        val value: C1,
    ) : EitherCart<C0, C1>()

    open fun deref(): Any? =
        when (this) {
            is A -> value
            is B -> value
        }
}

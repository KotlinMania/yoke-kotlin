// port-lint: source yoke.rs
package io.github.kotlinmania.yoke

interface CloneableCart

class Yoke<Y, C>(
    val yokeable: KindaSortaDangling<Y>,
    val cart: C,
    private val cloner: ((Y) -> Y)? = null,
) : CloneableCart {
    companion object {
        fun <Y, C> attachToCart(cart: C, f: (C) -> Y): Yoke<Y, C> {
            val deserialized = f(cart)
            return Yoke(KindaSortaDangling.new(deserialized), cart)
        }

        fun <Y, C, E> tryAttachToCart(cart: C, f: (C) -> Y): Yoke<Y, C> {
            val deserialized = f(cart)
            return Yoke(KindaSortaDangling.new(deserialized), cart)
        }

        fun <Y, C> attachToCartBadly(cart: C, f: (C) -> Y): Yoke<Y, C> = attachToCart(cart, f)

        fun <Y, C, E> tryAttachToCartBadly(cart: C, f: (C) -> Y): Yoke<Y, C> = tryAttachToCart<Y, C, E>(cart, f)

        fun <Y> newAlwaysOwned(yokeable: Y): Yoke<Y, Unit> = Yoke(KindaSortaDangling.new(yokeable), Unit)

        fun <Y, C> newOwned(yokeable: Y): Yoke<Y, C?> = Yoke(KindaSortaDangling.new(yokeable), null)

        fun <T> attachToZeroCopyCart(cart: T): Yoke<Cow<T>, T> =
            Yoke(
                KindaSortaDangling.new(Cow.Borrowed(cart)),
                cart,
                cloner = { cow ->
                    when (cow) {
                        is Cow.Borrowed -> Cow.Borrowed(cow.value)
                        is Cow.Owned -> Cow.Owned(cow.value)
                    }
                },
            )
    }

    fun attachToCart(cart: C, f: (C) -> Y): Yoke<Y, C> = Companion.attachToCart(cart, f)

    fun tryAttachToCart(cart: C, f: (C) -> Y): Yoke<Y, C> = Companion.attachToCart(cart, f)

    fun attachToCartBadly(cart: C, f: (C) -> Y): Yoke<Y, C> = Companion.attachToCart(cart, f)

    fun tryAttachToCartBadly(cart: C, f: (C) -> Y): Yoke<Y, C> = Companion.attachToCart(cart, f)

    fun attachToZeroCopyCart(cart: C): Yoke<Cow<C>, C> = Companion.attachToZeroCopyCart(cart)

    fun get(): Y = yokeable.deref()

    fun backingCart(): C = cart

    fun intoBackingCart(): C = cart

    fun <C2> replaceCart(f: (C) -> C2): Yoke<Y, C2> = Yoke(yokeable, f(cart))

    fun withMut(f: (Y) -> Unit) {
        f(yokeable.deref())
    }

    fun <R> withMutReturn(f: (Y) -> R): R = f(yokeable.deref())

    fun wrapCartInOption(): Yoke<Y, C?> = Yoke(yokeable, cart)

    fun newAlwaysOwned(yokeable: Y): Yoke<Y, Unit> = Companion.newAlwaysOwned(yokeable)

    fun intoYokeable(): Y = yokeable.intoInner()

    fun newOwned(yokeable: Y): Yoke<Y, C?> = Companion.newOwned(yokeable)

    fun tryIntoYokeable(): Y? = if (cart == null) yokeable.intoInner() else null

    fun convertCartIntoOptionPointer(): Yoke<Y, CartableOptionPointer<*>> {
        val ptr =
            if (cart != null && cart is CartablePointerLike) {
                CartableOptionPointer.fromCartable(cart)
            } else {
                CartableOptionPointer.none<BoxPointer<Unit>>()
            }
        return Yoke(yokeable, ptr)
    }

    fun clone(): Yoke<Y, C> {
        val current = yokeable.deref()
        val next = cloner?.invoke(current) ?: current
        return Yoke(KindaSortaDangling.new(next), cart, cloner)
    }

    fun <P> mapProject(f: (Y, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(yokeable.deref(), PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> mapProjectCloned(f: (Y, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(get(), PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> tryMapProject(f: (Y, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(yokeable.deref(), PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> tryMapProjectCloned(f: (Y, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(get(), PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P, T> mapProjectWithExplicitCapture(capture: T, f: (Y, T, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(yokeable.deref(), capture, PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P, T> mapProjectClonedWithExplicitCapture(capture: T, f: (Y, T, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(get(), capture, PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P, T> tryMapProjectWithExplicitCapture(capture: T, f: (Y, T, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(yokeable.deref(), capture, PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P, T> tryMapProjectClonedWithExplicitCapture(capture: T, f: (Y, T, PhantomData<Any?>) -> P): Yoke<P, C> {
        val p = f(get(), capture, PhantomData<Any?>())
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> mapWithCart(f: (Y, C) -> P): Yoke<P, C> {
        val p = f(yokeable.deref(), cart)
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> mapWithCartCloned(f: (Y, C) -> P): Yoke<P, C> {
        val p = f(get(), cart)
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> tryMapWithCart(f: (Y, C) -> P): Yoke<P, C> {
        val p = f(yokeable.deref(), cart)
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun <P> tryMapWithCartCloned(f: (Y, C) -> P): Yoke<P, C> {
        val p = f(get(), cart)
        return Yoke(KindaSortaDangling.new(p), cart)
    }

    fun eraseRcCart(): Yoke<Y, ErasedRcCart> = replaceCart { it as ErasedRcCart }

    fun eraseArcCart(): Yoke<Y, ErasedArcCart> = replaceCart { it as ErasedArcCart }

    fun eraseBoxCart(): Yoke<Y, ErasedBoxCart> = replaceCart { it as ErasedBoxCart }

    fun wrapCartInBox(): Yoke<Y, C> = this

    fun wrapCartInRc(): Yoke<Y, C> = this

    fun wrapCartInArc(): Yoke<Y, C> = this

    fun <B> wrapCartInEitherA(): Yoke<Y, EitherCart<C, B>> = replaceCart { EitherCart.A(it) }

    fun <A> wrapCartInEitherB(): Yoke<Y, EitherCart<A, C>> = replaceCart { EitherCart.B(it) }

    fun fmt(): String = "Yoke { yokeable: \"${get()}\", cart: \"${backingCart()}\" }"

    override fun toString(): String = fmt()
}

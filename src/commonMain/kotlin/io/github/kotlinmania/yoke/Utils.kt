// port-lint: source utils.rs
package io.github.kotlinmania.yoke

fun <Y, R> transformMutYokeable(yokeable: Y, f: (Y) -> R): R = f(yokeable)

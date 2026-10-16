# yoke-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fyoke-kotlin-blue.svg)](https://github.com/KotlinMania/yoke-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/yoke-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/yoke-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/yoke-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/yoke-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`yoke`](https://github.com/unicode-org/icu4x).

**Original Project:** This port is based on [`yoke`](https://github.com/unicode-org/icu4x). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `yoke`

> The text below is reproduced from [`https://github.com/unicode-org/icu4x`](https://github.com/unicode-org/icu4x). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs where appropriate so they continue to resolve from this repository.

# yoke [![crates.io](https://img.shields.io/crates/v/yoke)](https://crates.io/crates/yoke)

<!-- cargo-rdme start -->

This crate provides [`Yoke<Y, C>`][Yoke], which allows one to "yoke" (attach) a zero-copy deserialized
object (say, a [`Cow<'a, str>`](alloc::borrow::Cow)) to the source it was deserialized from, (say, an [`Rc<[u8]>`](alloc::rc::Rc)),
known in this crate as a "cart", producing a type that looks like `Yoke<Cow<'static, str>, Rc<[u8]>>`
and can be moved around with impunity.

Succinctly, this allows one to "erase" static lifetimes and turn them into dynamic ones, similarly
to how `dyn` allows one to "erase" static types and turn them into dynamic ones.

Most of the time the yokeable `Y` type will be some kind of zero-copy deserializable
abstraction, potentially with an owned variant (like [`Cow`](alloc::borrow::Cow),
[`ZeroVec`](https://docs.rs/zerovec), or an aggregate containing such types), and the cart `C` will be some smart pointer like
  [`Box<T>`](alloc::boxed::Box), [`Rc<T>`](alloc::rc::Rc), or [`Arc<T>`](std::sync::Arc), potentially wrapped in an [`Option<T>`](Option).

The key behind this crate is [`Yoke::get()`], where calling [`.get()`][Yoke::get] on a type like
`Yoke<Cow<'static, str>, _>` will get you a short-lived `&'a Cow<'a, str>`, restricted to the
lifetime of the borrow used during [`.get()`](Yoke::get). This is entirely safe since the `Cow` borrows from
the cart type `C`, which cannot be interfered with as long as the `Yoke` is borrowed by [`.get()`](Yoke::get).
[`.get()`](Yoke::get) protects access by essentially reifying the erased lifetime to a safe local one
when necessary.

See the documentation of [`Yoke`] for more details.

<!-- cargo-rdme end -->

## More Information

For more information on development, authorship, contributing etc. please visit [`ICU4X home page`](https://github.com/unicode-org/icu4x).

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:yoke-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same license terms as the upstream [`yoke`](https://github.com/unicode-org/icu4x). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the yoke authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`yoke`](https://github.com/unicode-org/icu4x) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.

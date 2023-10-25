package com.example.bank.common

import java.util.concurrent.TimeUnit

interface Cache<K, V> {
    val size: Int
    operator fun set(key: K, value: V)
    operator fun get(key: K): V?
    fun remove(key: K): V?
    fun clear()
}
class InMemoryCache<K, V>(
    private val delegate: MutableMap<K, V>,
    private val flushInterval: Long = TimeUnit.SECONDS.toMillis(60)
) : Cache<K, V> {
    private var lastFlushTime = System.nanoTime()

    override val size: Int
        get() = delegate.size

    override fun set(key: K, value: V) {
        delegate[key] = value
    }

    override fun remove(key: K): V? {
        recycle()
        return delegate.remove(key)
    }

    override fun get(key: K): V? {
        recycle()
        return delegate[key]
    }

    override fun clear() = delegate.clear()

    private fun recycle() {
        val shouldRecycle = System.nanoTime() - lastFlushTime >= TimeUnit.MILLISECONDS.toNanos(flushInterval)
        if (!shouldRecycle) return
        delegate.clear()
    }
}
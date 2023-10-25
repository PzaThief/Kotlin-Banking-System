package com.example.bank.common

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

class InMemoryCacheTests {
    @Test
    fun cacheShouldGetChannelBySavedKey() {
        runBlocking {
            val cache = InMemoryCache<Int, Channel<String>>(ConcurrentHashMap())
            val receiveWaitTime = 10.seconds
            val sendWaitTime = 5.seconds
            val key = 1
            val expectedSend = "test"

            val waitThread = CoroutineScope(Dispatchers.Default).launch {
                val channel = Channel<String>(capacity = Channel.CONFLATED)
                cache[key] = channel

                val received = withTimeoutOrNull(receiveWaitTime) { channel.receive() }
                cache.remove(key)
                assert(received == expectedSend)
            }

            val sendThread = CoroutineScope(Dispatchers.Default).launch {
                delay(sendWaitTime)
                cache[key]?.send(expectedSend)
            }

            waitThread.join()
            sendThread.join()
        }
    }

    @Test
    fun dataReceiveFailByTimeout() {
        runBlocking {
            val cache = InMemoryCache<Int, Channel<String>>(ConcurrentHashMap())
            val receiveWaitTime = 3.seconds
            val sendWaitTime = 5.seconds
            val key = 1
            val expectedSend = "test"

            val waitThread = CoroutineScope(Dispatchers.Default).launch {
                val channel = Channel<String>(capacity = Channel.CONFLATED)
                cache[key] = channel

                val received = withTimeoutOrNull(receiveWaitTime) { channel.receive() }
                cache.remove(key)
                assertThat(received).isNull()
            }

            val sendThread = CoroutineScope(Dispatchers.Default).launch {
                delay(sendWaitTime)
                cache[key]?.send(expectedSend)
            }

            waitThread.join()
            sendThread.join()
        }
    }

}
@file:OptIn(ExperimentalTime::class)
package util

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger

fun Logger.traceTime(startMessage: String, endMessage: String, block: suspend () -> Unit) {
    trace(startMessage)

    val duration = measureTime {
        runBlocking {
            block()
        }
    }

    trace("$endMessage (Took $duration.)")
}

fun Logger.debugTime(startMessage: String, endMessage: String, block: suspend () -> Unit) {
    debug(startMessage)

    val duration = measureTime {
        runBlocking {
            block()
        }
    }

    debug("$endMessage (Took $duration.)")
}

fun Logger.infoTime(startMessage: String, endMessage: String, block: suspend () -> Unit) {
    info(startMessage)

    val duration = measureTime {
        runBlocking {
            block()
        }
    }

    info("$endMessage (Took $duration.)")
}

fun Logger.warnTime(startMessage: String, endMessage: String, block: suspend () -> Unit) {
    warn(startMessage)

    val duration = measureTime {
        runBlocking {
            block()
        }
    }

    warn("$endMessage (Took $duration.)")
}

fun Logger.errorTime(startMessage: String, endMessage: String, block: suspend () -> Unit) {
    error(startMessage)

    val duration = measureTime {
        runBlocking {
            block()
        }
    }

    error("$endMessage (Took $duration.)")
}

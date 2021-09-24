package com.example

import java.lang.StringBuilder

/**
 * Class to maintain a list of log events
 */
class Logger {

    private var _log = StringBuilder()

    /**
     * Keep a log with [msg]
     */
    fun log(msg: String?) {
        _log.append(getTime())
        _log.append(">>  ")
        _log.append(msg)
        _log.append("\n")
    }

    /**
     * Keep a log with [msg] bounded with lines
     */
    fun logBoxed(msg: String?) {
        _log.append("\n")
        _log.append(getTime())
        _log.append(">>---------------OUTPUT-------------------<<\n\n")
        _log.append(msg)
        _log.append("\n")
        _log.append(getTime())
        _log.append(">>---------------OUTPUT-------------------<<\n")
    }

    /**
     * Clear the logs
     */
    fun clear() {
        _log = StringBuilder()
    }

    /**
     * Gets the logs
     */
    fun getLog() = _log.toString()
}
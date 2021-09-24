package com.example

/**
 * Callbacks from [Compiler] on code execution
 */
interface ExecutionCallbacks {

    /**
     * Event happened in code execution with [msg]
     */
    fun log(msg: String?)

    /**
     * Code execution completed with [result]
     */
    fun onResult(result: String?)
}
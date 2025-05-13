package com.projects.hanoipetadoption.data.util

/**
 * A generic class that holds a value or an error
 * @param <T> Type of the value
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    
    /**
     * Returns true if the result is successful
     */
    val isSuccess get() = this is Success
    
    /**
     * Returns true if the result is an error
     */
    val isError get() = this is Error
    
    /**
     * Returns the value or null if the result is an error
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }
    
    /**
     * Maps the value if the result is successful
     */
    fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(exception)
        }
    }
    
    /**
     * Returns the value or throws the exception if the result is an error
     */
    fun getOrThrow(): T {
        when (this) {
            is Success -> return data
            is Error -> throw exception
        }
    }
    
    /**
     * Executes the given block if the result is successful
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Executes the given block if the result is an error
     */
    inline fun onError(action: (Exception) -> Unit): Result<T> {
        if (this is Error) action(exception)
        return this
    }
    
    companion object {
        /**
         * Creates a successful result with the given value
         */
        fun <T> success(data: T): Result<T> = Success(data)
        
        /**
         * Creates an error result with the given exception
         */
        fun <T> error(exception: Exception): Result<T> = Error(exception)
        
        /**
         * Converts a nullable value to a Result
         */
        fun <T : Any> of(value: T?): Result<T> {
            return if (value != null) {
                success(value)
            } else {
                error(NullPointerException("Value is null"))
            }
        }
    }
}

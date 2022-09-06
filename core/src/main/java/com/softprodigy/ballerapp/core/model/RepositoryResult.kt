package  com.softprodigy.ballerapp.core.model

sealed class RepositoryResult<out T : Any> {

    data class Success<out T : Any>(val data: T?) : RepositoryResult<T>()
    data class Error(val exception: String, val code: Int? = 0) : RepositoryResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
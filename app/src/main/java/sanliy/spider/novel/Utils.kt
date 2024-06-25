package sanliy.spider.novel


enum class TripleSwitch {
    TRUE {
        override fun nextKey(): TripleSwitch {
            return FALSE
        }
    },
    FALSE {
        override fun nextKey(): TripleSwitch {
            return NULL
        }
    },
    NULL {
        override fun nextKey(): TripleSwitch {
            return TRUE
        }
    };

    abstract fun nextKey(): TripleSwitch
}

fun tripleSwitch(input: Int): Int {
    return when (input) {
        0 -> 1
        1 -> -1
        -1 -> 0
        else -> throw IllegalArgumentException("Invalid input")
    }
}

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure(val throwable: Throwable) : UiState<Nothing>()
}

inline fun <T> UiState<T>.onState(
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit = {},
    onLoading: () -> Unit = {},
) {
    when (this) {
        UiState.Loading -> onLoading()
        is UiState.Success -> onSuccess(data)
        is UiState.Failure -> onFailure(throwable)
    }
}

inline fun <T> UiState<T>.onSuccess(
    onSuccess: (T) -> Unit,
): UiState<T> {
    if (this is UiState.Success) {
        onSuccess(data)
    }
    return this
}

inline fun <T> UiState<T>.onFailure(
    onFailure: (Throwable) -> Unit,
): UiState<T> {
    if (this is UiState.Failure) {
        onFailure(throwable)
    }
    return this
}

inline fun <T> UiState<T>.onLoading(
    onLoading: () -> Unit,
): UiState<T> {
    if (this is UiState.Loading) {
        onLoading()
    }
    return this
}

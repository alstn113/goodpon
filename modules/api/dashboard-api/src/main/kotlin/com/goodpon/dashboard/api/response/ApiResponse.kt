package com.goodpon.dashboard.api.response

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(error: ErrorType, errorData: Any? = null): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(error, errorData))
        }

        fun <S> error(error: ErrorType, details: List<ApiErrorDetail>): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(error, details))
        }
    }
}

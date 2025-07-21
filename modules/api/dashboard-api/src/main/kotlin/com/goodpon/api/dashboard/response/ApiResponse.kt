package com.goodpon.api.dashboard.response

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun error(error: ErrorType, errorData: Any? = null): ApiResponse<Unit> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(error, errorData))
        }

        fun error(error: ErrorType, details: List<ApiErrorDetail>): ApiResponse<Unit> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(error, details))
        }
    }
}

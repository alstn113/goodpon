package com.goodpon.dashboard.api.response

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val errorMessage: String? = null,
) {

    companion object {
        fun success(): ApiResponse<Unit> {
            return ApiResponse(ResultType.SUCCESS, null, null)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(errorMessage: String, errorData: Any? = null): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, errorMessage)
        }
    }
}

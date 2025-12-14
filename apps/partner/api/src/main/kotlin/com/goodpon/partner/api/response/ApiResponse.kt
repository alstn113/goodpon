package com.goodpon.partner.api.response

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                result = ResultType.SUCCESS,
                data = data
            )
        }

        fun error(error: ErrorType, errorData: Any? = null): ApiResponse<Unit> {
            return ApiResponse(
                result = ResultType.ERROR,
                error = ErrorMessage(error, errorData),
            )
        }
    }
}
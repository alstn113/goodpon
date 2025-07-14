package com.goodpon.partner.openapi.response

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
    val traceId: String? = null, // filter 에서 자동으로 주입
) {

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                result = ResultType.SUCCESS,
                data = data
            )
        }

        fun error(error: ErrorType, errorData: Any? = null, traceId: String? = null): ApiResponse<Unit> {
            return ApiResponse(
                result = ResultType.ERROR,
                error = ErrorMessage(error, errorData),
                traceId = traceId
            )
        }
    }
}
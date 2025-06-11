package com.goodpon.api.dashboard.api.response

import com.goodpon.common.support.error.ErrorMessage
import com.goodpon.common.support.error.ErrorType


data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {

    companion object {
        fun success(): ApiResponse<Unit> {
            return ApiResponse(ResultType.SUCCESS, null, null)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(errorType: ErrorType, errorData: Any? = null): ApiResponse<S> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(errorType))
        }
    }
}

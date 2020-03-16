/*
 * Copyright 2018 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package literefresh.sample.base.model

class Resource<T>(val status: Status, val data: T?, val message: String?, val throwable: Throwable?) {
    /**
     * Status of a resource that is provided to the UI.
     *
     *
     * These are usually created by the Repository classes where they return
     * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
     */
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    val isError: Boolean
        get() = status == Status.ERROR

    val isSuccess: Boolean
        get() = status == Status.SUCCESS

    val isLoading: Boolean
        get() = status == Status.LOADING

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val resource = o as Resource<*>
        if (status != resource.status) {
            return false
        }
        if (if (message != null) message != resource.message else resource.message != null) {
            return false
        }
        if (if (data != null) data != resource.data else resource.data != null) {
            return false
        }
        return if (throwable != null) throwable == resource.throwable else resource.throwable == null
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (throwable?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", throwable=" + throwable +
                '}'
    }

    companion object {
        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null, null)
        }

        fun <T> loading(data: T): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }

        fun <T> success(): Resource<T?> {
            return Resource(Status.SUCCESS, null, null, null)
        }

        fun <T> success(data: T): Resource<T> {
            return success(data, null)
        }

        fun <T> success(data: T, message: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, message, null)
        }

        fun <T> error(): Resource<T> {
            return error(null, null, null)
        }

        fun <T> error(msg: String?, data: T?, throwable: Throwable?): Resource<T> {
            return Resource(Status.ERROR, data, msg, throwable)
        }

        fun <T> error(msg: String): Resource<T> {
            return error(msg, null, null)
        }

        fun <T> error(throwable: Throwable): Resource<T> {
            return error(null, null, throwable)
        }
    }

}
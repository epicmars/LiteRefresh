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
package com.androidpi.literefresh.sample.base.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resource<T> {

    /**
     * Status of a resource that is provided to the UI.
     * <p>
     * These are usually created by the Repository classes where they return
     * {@code LiveData<Resource<T>>} to pass back the latest data to the UI with its fetch status.
     */
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable throwable;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message, @Nullable Throwable throwable) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.throwable = throwable;
    }


    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null, null);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null, null);
    }

    public static <T> Resource<T> success() {
        return new Resource<>(Status.SUCCESS, null, null, null);
    }

    public static <T> Resource<T> success(T data) {
        return success(data, null);
    }

    public static <T> Resource<T> success(T data, String message) {
        return new Resource<>(Status.SUCCESS, data, message, null);
    }

    public static <T> Resource<T> error() {
        return new Resource<>(Status.ERROR, null, null, null);
    }

    public static <T> Resource<T> error(@Nullable String msg, @Nullable T data, @Nullable Throwable throwable) {
        return new Resource<>(Status.ERROR, data, msg, throwable);
    }

    public static <T> Resource<T> error(@NonNull String msg) {
        return error(msg, null, null);
    }

    public static <T> Resource<T> error(@NonNull Throwable throwable) {
        return error(null, null, throwable);
    }

    public boolean isError() {
        return status == Status.ERROR;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (message != null ? !message.equals(resource.message) : resource.message != null) {
            return false;
        }
        if (data != null ? !data.equals(resource.data) : resource.data != null) {
            return false;
        }
        return throwable != null ? throwable.equals(resource.throwable) : resource.throwable == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", throwable=" + throwable +
                '}';
    }
}
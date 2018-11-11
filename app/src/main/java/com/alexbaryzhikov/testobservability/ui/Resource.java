package com.alexbaryzhikov.testobservability.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class Resource<T> {
    private final T result;

    public Resource(T result) {
        this.result = result;
    }

    T getResult() {
        return result;
    }

    static final class Success<T> extends Resource<T> {
        Success(@NonNull T result) {
            super(result);
        }
    }

    static final class Failure<T> extends Resource<T> {
        Failure(@Nullable T result) {
            super(result);
        }
    }

    static final class Loading<T> extends Resource<T> {
        Loading() {
            super(null);
        }
    }
}

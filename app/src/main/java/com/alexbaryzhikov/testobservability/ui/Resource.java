package com.alexbaryzhikov.testobservability.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resource<T> {
  @NonNull private final Status status;
  @Nullable private final T data;

  private Resource(@NonNull Status status, @Nullable T data) {
    this.status = status;
    this.data = data;
  }

  public static <T> Resource<T> success(@NonNull T data) {
    return new Resource<>(Status.SUCCESS, data);
  }

  public static <T> Resource<T> error(@Nullable T data) {
    return new Resource<>(Status.ERROR, data);
  }

  public static <T> Resource<T> loading(@Nullable T data) {
    return new Resource<>(Status.LOADING, data);
  }

  @NonNull
  public Status getStatus() {
    return status;
  }

  @Nullable
  public T getData() {
    return data;
  }

  public enum Status {SUCCESS, ERROR, LOADING}
}

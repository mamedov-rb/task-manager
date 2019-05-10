package ru.rmamedov.app.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBaseService<T> {

    @NotNull
    T findById(@NotNull final String id);

    @NotNull
    List<T> findAll();

    @NotNull
    @Transactional
    T save(@NotNull final T t);

    @Transactional
    void deleteById(@NotNull final String id);

    @Transactional
    void deleteAll();

    @NotNull
    @Transactional
    T update(@NotNull final T t);

    @NotNull
    @Transactional
    T patch(@NotNull final T t);
}

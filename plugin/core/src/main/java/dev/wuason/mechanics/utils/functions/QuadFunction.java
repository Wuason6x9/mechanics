package dev.wuason.mechanics.utils.functions;

import java.util.function.Function;


@FunctionalInterface
public interface QuadFunction<T, U, V, I, R> {

    R apply(T t, U u, V v, I i);

    default <W> QuadFunction<T, U, V, I, W> andThen(Function<? super R, ? extends W> after) {
        return (t, u, v, i) -> after.apply(apply(t, u, v, i));
    }
}
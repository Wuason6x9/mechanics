package dev.wuason.mechanics.utils.functions;

import java.util.Objects;

@FunctionalInterface
public interface QuadConsumer<T, U, V, I> {

    void accept(T t, U u, V v, I i);

    default QuadConsumer<T, U, V, I> andThen(QuadConsumer<? super T, ? super U, ? super V, ? super I> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}
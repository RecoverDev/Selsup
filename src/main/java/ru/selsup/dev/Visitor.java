package ru.selsup.dev;

import java.util.List;

@FunctionalInterface
public interface Visitor<E> {
    void out(List<E> values);
}

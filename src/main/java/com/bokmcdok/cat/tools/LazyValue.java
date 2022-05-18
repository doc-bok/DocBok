package com.bokmcdok.cat.tools;

import java.util.function.Supplier;

/**
 * Use when you know a resource is available on access, but not when you
 * instance an object.
 * @param <T> The type of value that is lazy.
 */
public class LazyValue<T> {
    private Supplier<T> supplier;
    private T value;

    /**
     * Creates a lazy value.
     * @param supplier The object that will supply the value when it is needed.
     */
    public LazyValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Get the actual value.
     * @return The required resource, provided it exists at this point.
     */
    public T getValue() {
        Supplier<T> supplier = this.supplier;
        if (supplier != null) {
            this.value = supplier.get();
            this.supplier = null;
        }

        return this.value;
    }
}
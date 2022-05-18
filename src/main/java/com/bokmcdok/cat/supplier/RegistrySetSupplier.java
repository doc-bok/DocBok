package com.bokmcdok.cat.supplier;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Set;
import java.util.function.Supplier;

public record RegistrySetSupplier<T extends IForgeRegistryEntry<T>>
        (IForgeRegistry<T> registry,
         Set<ResourceLocation> resourceLocations)
        implements Supplier<Set<T>> {

    /**
     * Get the values.
     *
     * @return A set of objects pulled from the registry.
     */
    @Override
    public Set<T> get() {
        Set<T> result = Sets.newHashSet();
        for (ResourceLocation location : resourceLocations) {
            T entry = registry.getValue(location);
            if (entry != null) {
                result.add(entry);
            }
        }

        return result;
    }
}

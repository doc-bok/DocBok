package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventList {

    //  Our sound event registry
    public static DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CatMod.MOD_ID);

    //  The Nyan Cat music
    public static final RegistryObject<SoundEvent> NYAN_CAT_MUSIC =
            registerSoundEvent("nyan_cat_ambient");

    //  Helper function for registering sound events
    private static RegistryObject<SoundEvent> registerSoundEvent(String location) {
        return SOUND_EVENTS.register(location, () ->
                new SoundEvent(new ResourceLocation(CatMod.MOD_ID, location)));

    }
}

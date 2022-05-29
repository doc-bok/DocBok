package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.blocks.BottledButterflyBlock;
import com.bokmcdok.cat.util.BlockUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * A list of new blocks added by the mod
 */
public class BlockList {

    //  The block registry
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CatMod.MOD_ID);

    public static final RegistryObject<Block> BOTTLED_BUTTERFLY = BLOCKS.register(
            BottledButterflyBlock.NAME, () -> new BottledButterflyBlock(
                    BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.COLOR_LIGHT_BLUE)
                            .strength(0.3F)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .isValidSpawn(BlockUtil::never)
                            .isRedstoneConductor(BlockUtil::never)
                            .isSuffocating(BlockUtil::never)
                            .isViewBlocking(BlockUtil::never))
    );
}

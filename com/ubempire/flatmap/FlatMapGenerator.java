package com.ubempire.flatmap;
//matter123

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FlatMapGenerator extends ChunkGenerator {
    public byte[] generate(World world, Random random, int cx, int cz) {
        byte[] result = new byte[32768];

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 64; y++) {
                    if (y < 2) {
                        result[(x * 16 + z) * 128 + y] = (byte) Material.BEDROCK.getId();
                    } else if (y < 63) {
                        result[(x * 16 + z) * 128 + y] = (byte) Material.STONE.getId();
                    } else {
                        result[(x * 16 + z) * 128 + y] = (byte) Material.GRASS.getId();
                    }
                }
            }
        }

        return result;
    }

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator) new FlatMapPopulator(), new EasterEgg());
    }

    public boolean canSpawn(World world, int x, int z) {
        return true;
    }
}
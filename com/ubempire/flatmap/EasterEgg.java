package com.ubempire.flatmap;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.Random;

public class EasterEgg extends BlockPopulator {
    private static final int Pyrmid_CHANCE = 1; // Out of 200
    private String[] letters = new String[30];

    public String getRandomString() {
        letters[0] = "a";
        letters[1] = "b";
        letters[2] = "c";
        letters[3] = "d";
        letters[4] = "e";
        letters[5] = "f";
        letters[6] = "g";
        letters[7] = "h";
        letters[8] = "i";
        letters[9] = "j";
        letters[10] = "k";
        letters[11] = "l";
        letters[12] = "m";
        letters[13] = "n";
        letters[14] = "o";
        letters[15] = "p";
        letters[16] = "q";
        letters[17] = "r";
        letters[18] = "s";
        letters[19] = "t";
        letters[20] = "u";
        letters[21] = "v";
        letters[22] = "w";
        letters[23] = "x";
        letters[24] = "y";
        letters[25] = "z";
        letters[26] = " ";
        letters[27] = " ";
        letters[28] = " ";
        letters[29] = " ";
        String mystring = "";
        for (int i = 0; i <= 16; i++) {
            mystring = mystring + letters[(int) (Math.random() * 30)];
        }
        return mystring;
    }

    ArrayList<Block> Currentsnakeblocks = new ArrayList<Block>();

    @Override
    public void populate(World world, Random random, Chunk source) {
        if (random.nextInt(800) <= Pyrmid_CHANCE || (source.getX() == 0 && source.getZ() == 0)) {
            int centerX = (source.getX() << 4) + random.nextInt(16);
            int centerZ = (source.getZ() << 4) + random.nextInt(16);
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            int radius = random.nextInt(8) + 15;
            boolean rg = false;
            Block rm = null;
            for (int Y = centerY; radius >= 0; Y++, radius--) {
                for (int x = -radius + centerX; x <= radius + centerX; x++) {
                    for (int z = -radius + centerZ; z <= radius + centerZ; z++) {
                        if (Y == centerY) {
                            if (!rg) {
                                if (x >= -radius + centerX + 9 && x <= radius + centerX - 9 && z >= -radius + centerZ + 9 && z <= radius + centerZ - 9) {
                                    if (Math.random() > 0.95) {
                                        rm = world.getBlockAt(x, Y - 5, z);
                                        rg = true;
                                    }
                                }
                            }
                            world.getBlockAt(x, Y - 1, z).setType(Material.SAND);
                        }
                        if (x >= -radius + centerX || x <= radius + centerX || z >= -radius + centerZ || z <= radius + centerZ) {
                            if (Math.random() < 0.1 && Y > centerY + 3) world.getBlockAt(x, Y, z).setType(Material.AIR);
                            else world.getBlockAt(x, Y, z).setType(Material.SANDSTONE);
                        }
                    }
                }
            }
            if (rm == null) {
                rm = world.getBlockAt(centerX, centerY - 5, centerZ);
            }
            startsnake(world, random, rm);
            startsnake(world, random, world.getBlockAt(centerX, centerY, centerZ));
            //startsnake(world,random,world.getBlockAt(centerX,centerY+5,centerZ));
            // System.out.print(Currentsnakeblocks.size());
            for (Block snake : Currentsnakeblocks) {
                //world.getBlockAt(snake.getLocation().getBlockX(),snake.getLocation().getBlockY()+2,snake.getLocation().getBlockZ()).setType(Material.SANDSTONE);
                //world.getBlockAt(snake.getLocation().getBlockX(),snake.getLocation().getBlockY()-1,snake.getLocation().getBlockZ()).setTypeId(0);
                world.getBlockAt(snake.getLocation().getBlockX(), snake.getLocation().getBlockY() + 1, snake.getLocation().getBlockZ()).setTypeId(0);
                world.getBlockAt(snake.getLocation().getBlockX(), snake.getLocation().getBlockY() + 2, snake.getLocation().getBlockZ()).setTypeId(0);
                snake.setTypeId(0);
            }
            Currentsnakeblocks.clear();
            if (rm != null) {
                for (int x = rm.getLocation().getBlockX() - 3; x <= rm.getLocation().getBlockX() + 3; x++) {
                    for (int y = rm.getLocation().getBlockY() - 1; y <= rm.getLocation().getBlockY() + 2; y++) {
                        for (int z = rm.getLocation().getBlockZ() - 3; z <= rm.getLocation().getBlockZ() + 3; z++) {
                            if (y == rm.getLocation().getBlockY() - 1) world.getBlockAt(x, y, z).setTypeId(41);
                            else if (y == rm.getLocation().getBlockY() && x == rm.getLocation().getBlockX() && z == rm.getLocation().getBlockZ()) {
                                world.getBlockAt(x, y, z).setTypeId(63);
                                Sign state = (Sign) world.getBlockAt(x, y, z).getState();
                                world.getBlockAt(x - 1, y, z - 1).setTypeId(50);
                                state.setLine(1, getRandomString());
                                state.setLine(2, getRandomString());

                            } else world.getBlockAt(x, y, z).setTypeId(0);
                            if ((z == rm.getLocation().getBlockZ() - 3 || z == rm.getLocation().getBlockZ() + 3) || (x == rm.getLocation().getBlockX() - 3 || x == rm.getLocation().getBlockX() + 3)) {
                                if (Math.random() > 0.5) {
                                    world.getBlockAt(x, y, z).setType(Material.SANDSTONE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void startsnake(World world, Random random, Block block) {
        int blockX = block.getX();
        int blockY = block.getY();
        int blockZ = block.getZ();
        while (world.getBlockAt(blockX, blockY, blockZ).getTypeId() != 0) {
            if (blockY < 64 && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
                blockY = blockY + 1;
            }
            if (world.getBlockAt(blockX + 2, blockY, blockZ).getTypeId() == 0) {
                blockX = blockX + 1;
            } else if (world.getBlockAt(blockX - 2, blockY, blockZ).getTypeId() == 0) {
                blockX = blockX - 1;
            } else if (world.getBlockAt(blockX, blockY, blockZ + 2).getTypeId() == 0) {
                blockZ = blockZ + 1;
            } else if (world.getBlockAt(blockX, blockY, blockZ - 2).getTypeId() == 0) {
                blockZ = blockZ - 1;
            } else if (world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId() == 0) {
                blockX = blockX + 1;

            } else if (world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId() == 0) {
                blockX = blockX - 1;

            } else if (world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId() == 0) {
                blockZ = blockZ + 1;

            } else if (world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId() == 0) {
                blockZ = blockZ - 1;

            } else if (random.nextBoolean()) {
                if (random.nextBoolean()) blockX = blockX + 1;
                else blockZ = blockZ + 1;
            } else {
                if (random.nextBoolean()) blockX = blockX - 1;
                else blockZ = blockZ - 1;
            }
            //if(Math.random()>0.995) startsnake(world,random,world.getBlockAt(blockX,blockY,blockZ));
            if (world.getBlockAt(blockX, blockY, blockZ).getTypeId() != 0)
                Currentsnakeblocks.add(world.getBlockAt(blockX, blockY, blockZ));
        }
    }
}
package apritree.worldgen.gen;

import apritree.block.saplings.BlockSaplingUltra;
import apritree.config.ApriConfig;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import apritree.ApriRegistry;
import apritree.block.saplings.BlockApricornSapling;

import java.util.*;

public class ApricornTreeGenerator implements IWorldGenerator {
    public static ApricornTreeGenerator INSTANCE = new ApricornTreeGenerator();

    protected IBlockState air = Blocks.AIR.getDefaultState();
    private IBlockState[] saplings;

    private ApricornTreeGenerator() {
        saplings = new IBlockState[7];
        for (int i = 0; i < 7; i++) {
            saplings[i] = ApriRegistry.apricornSapling.getStateFromMeta(i);
        }
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider provider) {
        if (world.provider.getDimension() != 0) {
            if (ApriConfig.beastBallCrafting && world.provider.getDimension() == UltraSpace.DIM_ID)
                generateUltraSpace(rand, chunkX, chunkZ, world);
            else
                return;
        }
        int randChance = world.getWorldType() == WorldType.FLAT ? 100 : 10;
        if (rand.nextInt(randChance) != 0) return;
        List<IBlockState> states = new ArrayList<IBlockState>();
        int x = chunkX * 16 + 8 + rand.nextInt(16);
        int z = chunkZ * 16 + 8 + rand.nextInt(16);
        int y = 128;
        BlockPos pos = findGround(world, x, y, z);

        if (isGround(world, pos.down()) && isReplacable(world, pos) && isAir(world, pos.up())) {
            if (world.getWorldType() == WorldType.FLAT) {
                for (int i = 0; i < saplings.length; i++)
                    states.add(saplings[i]);
            } else {
                for (int i = 0; i < saplings.length; i++) {
                    Biome biome = world.getBiome(pos);
                    Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
                    for (BiomeDictionary.Type type : types) {
                        if (Arrays.asList(saplings[i].getValue(BlockApricornSapling.APRICORNS).getBiomeTypes()).contains(type)) {
                            states.add(saplings[i]);
                        }
                    }
                }
            }
            if (states.size() > 0) {
                IBlockState saplingState = states.get(rand.nextInt(states.size()));
                ((BlockApricornSapling) saplingState.getBlock()).generateTree(world, pos, saplingState, rand);
            }
        }
    }

    private void generateUltraSpace(Random rand, int chunkX, int chunkZ, World world) {
        if (rand.nextInt(10) != 0) return;
        int x = chunkX * 16 + 8 + rand.nextInt(16);
        int z = chunkZ * 16 + 8 + rand.nextInt(16);
        int y = world.getHeight(new BlockPos(x, 0, z)).getY();
        BlockPos pos = findGround(world, x, y, z);
        if (isGround(world, pos.down()) && isReplacable(world, pos) && isAir(world, pos.up())) {
            ((BlockSaplingUltra)ApriRegistry.apricornSaplingUltra).generateTree(world, pos, ApriRegistry.apricornSaplingUltra.getDefaultState(), rand);
        }
    }

    private boolean isAir(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos);
    }

    private boolean isReplacable(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isReplaceable(world, pos) || state.getBlock().isAir(state, world, pos);
    }

    private boolean isGround(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return (state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS) && !world.getBlockState(pos.up()).isFullBlock();
    }

    private BlockPos findGround(World world, int x, int y, int z) {
        int height = y;
        boolean groundFound = false;
        int minHeight = world.getWorldType() == WorldType.FLAT ? 3 : 64;
        while (height > minHeight && !groundFound) {
            BlockPos pos = new BlockPos(x, height, z);
            if (!isGround(world, pos))
                height--;
            else
                groundFound = true;
        }
        return new BlockPos(x, height + 1, z);
    }
}

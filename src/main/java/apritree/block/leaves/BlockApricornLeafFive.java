package apritree.block.leaves;

import apritree.ApriRegistry;
import apritree.block.BlockApriLeafBase;
import apritree.block.EnumApricorns;
import apritree.block.StateLibrary;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockApricornLeafFive extends BlockApriLeafBase {
    public BlockApricornLeafFive() {
        super();
        this.setTranslationKey("apritree:apricorn_leaf_fifth");
        this.setDefaultState(this.blockState.getBaseState().withProperty(StateLibrary.APRICORNS5, EnumApricorns.GILDED).withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
    }

    @Override
    public EnumApricorns getApricornFromState(IBlockState state) {
        return state.getValue(StateLibrary.APRICORNS5);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(StateLibrary.APRICORNS5).getMeta() - 16;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ApriRegistry.apricornSaplingSpecial);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state) & 3);
    }

    public EnumApricorns getApricornType(int meta)
    {
        return EnumApricorns.byMeta((meta % 4) + 16);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return java.util.Arrays.asList(new ItemStack(this, 1, world.getBlockState(pos).getValue(StateLibrary.APRICORNS5).getMeta() - 16));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(StateLibrary.APRICORNS5, getApricornType(meta)).withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(StateLibrary.APRICORNS5).getMeta() - 16;

        if (!state.getValue(DECAYABLE))
        {
            i |= 4;
        }

        if (state.getValue(CHECK_DECAY))
        {
            i |= 8;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, StateLibrary.APRICORNS5, CHECK_DECAY, DECAYABLE);
    }
}

package pixelplus.item;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemApricorn extends Item
{
    //Uncooked, cooked
    public ItemApricorn()
    {
        super();
        this.setUnlocalizedName("pixelplus:apricorn");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(PixelmonCreativeTabs.natural);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for(int i = 0; i < 2; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}

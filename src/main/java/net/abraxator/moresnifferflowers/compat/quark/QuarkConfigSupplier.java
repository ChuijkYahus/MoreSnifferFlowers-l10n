package net.abraxator.moresnifferflowers.compat.quark;

import net.minecraftforge.fml.ModList;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.module.AncientWoodModule;

public class QuarkConfigSupplier {

    public static int sniffingLootWeight(){
        if (ModList.get().isLoaded("quark") && Quark.ZETA.modules.isEnabled(AncientWoodModule.class)){
          return AncientWoodModule.sniffingLootWeight;
        }
        return 0;
    }
}

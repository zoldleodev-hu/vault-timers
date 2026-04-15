package hu.zoldleo.vault_timers;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(VaultTimers.MODID)
public class VaultTimers {
    public static final String MODID = "vault_timers";

    public VaultTimers(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
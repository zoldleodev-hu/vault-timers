package hu.zoldleo.vault_timers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(VaultTimers.MODID)
public class VaultTimers {
    public static final String MODID = "vault_timers";

    public VaultTimers(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(Config::onLoad);
        modEventBus.addListener(Config::onReload);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }
}
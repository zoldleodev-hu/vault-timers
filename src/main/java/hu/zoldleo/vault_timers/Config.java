package hu.zoldleo.vault_timers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.LongValue NORMAL_TIME = BUILDER.comment("Reset timer for normal vaults in ticks").defineInRange("normal_time", 72_000L, 0, Long.MAX_VALUE); // 1 hour
    private static final ModConfigSpec.LongValue OMINOUS_TIME = BUILDER.comment("Reset timer for ominous vaults in ticks").defineInRange("ominous_time", 360_000L, 0, Long.MAX_VALUE); // 5 hours

    static final ModConfigSpec SPEC = BUILDER.build();

    public static long normal_time;
    public static long ominous_time;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        normal_time = NORMAL_TIME.get();
        ominous_time = OMINOUS_TIME.get();
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        normal_time = NORMAL_TIME.get();
        ominous_time = OMINOUS_TIME.get();
    }
}
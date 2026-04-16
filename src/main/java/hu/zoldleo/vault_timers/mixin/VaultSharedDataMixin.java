package hu.zoldleo.vault_timers.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import hu.zoldleo.vault_timers.TimerVaultServerData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.UUID;
import java.util.function.Predicate;

@Mixin(VaultSharedData.class)
public class VaultSharedDataMixin {
    @ModifyArg(method = "updateConnectedPlayersWithinRange", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"))
    private static Predicate<UUID> getConnectedPlayersByTime(Predicate<UUID> predicate, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) VaultServerData serverData) {
        if (serverData instanceof TimerVaultServerData data)
            return uuid -> !data.hasRewardedPlayer(uuid, level.getGameTime());
        return predicate;
    }
}
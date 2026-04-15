package hu.zoldleo.vault_timers.mixin;

import hu.zoldleo.vault_timers.TimerVaultServerData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VaultBlockEntity.Server.class)
public class VaultBlockEntityServerMixin {
    @Inject(method = "tryInsertKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/vault/VaultServerData;addToRewardedPlayers(Lnet/minecraft/world/entity/player/Player;)V"))
    private static void setOminous(ServerLevel level, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player, ItemStack stack, CallbackInfo ci) {
        if (serverData instanceof TimerVaultServerData data)
            data.ominous = state.getValue(VaultBlock.OMINOUS);
    }
}
package hu.zoldleo.vault_timers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import hu.zoldleo.vault_timers.TimerVaultServerData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VaultBlockEntity.class)
public class VaultBlockEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void swapData(BlockPos pos, BlockState state, CallbackInfo ci) {
        ((VaultBlockEntity)(Object)this).serverData = new TimerVaultServerData();
    }

    @ModifyArg(method = "saveAdditional", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/vault/VaultBlockEntity;encode(Lcom/mojang/serialization/Codec;Ljava/lang/Object;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/nbt/Tag;", ordinal = 2))
    public Codec<TimerVaultServerData> saveData(Codec<TimerVaultServerData> input) {
        return TimerVaultServerData.CODEC;
    }

    @ModifyReceiver(method = "loadAdditional", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", ordinal = 0))
    public Codec<TimerVaultServerData> loadData(Codec<VaultServerData> instance, DynamicOps<Tag> dynamicOps, Object o) {
        return TimerVaultServerData.CODEC;
    }
}

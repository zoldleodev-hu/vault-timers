package hu.zoldleo.vault_timers.mixin;

import com.mojang.serialization.Codec;
import hu.zoldleo.vault_timers.TimerVaultServerData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
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

    @ModifyArg(method = "saveAdditional", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ValueOutput;store(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V", ordinal = 2))
    public Codec<TimerVaultServerData> saveData(Codec<TimerVaultServerData> codec) {
        return TimerVaultServerData.CODEC;
    }

    @ModifyArg(method = "loadAdditional", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ValueInput;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;", ordinal = 0))
    public Codec<TimerVaultServerData> loadData(Codec<TimerVaultServerData> codec) {
        return TimerVaultServerData.CODEC;
    }
}
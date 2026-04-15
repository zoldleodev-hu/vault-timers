package hu.zoldleo.vault_timers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TimerVaultServerData extends VaultServerData {
    public static Codec<TimerVaultServerData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("rewarded_players", Set.of()).forGetter((data) -> data.playerTimers.keySet()), Codec.LONG.listOf().lenientOptionalFieldOf("player_timers", List.of()).forGetter((data) -> new ArrayList<>(data.playerTimers.values())), Codec.LONG.lenientOptionalFieldOf("state_updating_resumes_at", 0L).forGetter((data) -> data.stateUpdatingResumesAt), ItemStack.CODEC.listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter((data) -> data.itemsToEject), Codec.INT.lenientOptionalFieldOf("total_ejections_needed", 0).forGetter((data) -> data.totalEjectionsNeeded)).apply(instance, TimerVaultServerData::new));
    public Map<UUID, Long> playerTimers = new HashMap<>();
    public boolean ominous = false;

    public TimerVaultServerData(Set<UUID> rewardedPlayers, List<Long> timers, long stateUpdatingResumesAt, List<ItemStack> itemsToEject, int totalEjectionsNeeded) {
        UUID[] playerArray = rewardedPlayers.toArray(new UUID[0]);
        Long[] timerArray = timers.toArray(new Long[0]);
        for (int i = 0; i < playerArray.length; i++) {
            if (i < timerArray.length)
                playerTimers.put(playerArray[i], timerArray[i]);
            else
                playerTimers.put(playerArray[i], 0L);
        }
        this.stateUpdatingResumesAt = stateUpdatingResumesAt;
        this.itemsToEject.addAll(itemsToEject);
        this.totalEjectionsNeeded = totalEjectionsNeeded;
    }

    public TimerVaultServerData() {
    }

    @Override
    public @NotNull Set<UUID> getRewardedPlayers() {
        return playerTimers.keySet();
    }

    @Override
    public boolean hasRewardedPlayer(@NotNull Player player) {
        return playerTimers.containsKey(player.getUUID()) &&
                player.level().getGameTime() < playerTimers.get(player.getUUID());
    }

    @Override
    public void addToRewardedPlayers(@NotNull Player player) {
        playerTimers.put(player.getUUID(), player.level().getGameTime() + (ominous ? Config.ominous_time : Config.normal_time));
        if (playerTimers.size() > 128) {
            Iterator<UUID> iterator = playerTimers.keySet().iterator();
            if (iterator.hasNext())
                playerTimers.remove(iterator.next());
        }
        markChanged();
    }

    @Override
    public void set(VaultServerData other) {
        stateUpdatingResumesAt = other.stateUpdatingResumesAt();
        itemsToEject.clear();
        itemsToEject.addAll(other.itemsToEject);
        playerTimers.clear();
        if (other instanceof TimerVaultServerData data) {
            playerTimers.putAll(data.playerTimers);
            return;
        }
        for (UUID player : other.rewardedPlayers) {
            playerTimers.put(player, 0L);
        }
    }
}
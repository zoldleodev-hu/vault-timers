package hu.zoldleo.vault_timers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TimerVaultServerData extends VaultServerData {
    public static Codec<TimerVaultServerData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("rewarded_players", Set.of()).forGetter((data) -> data.rewardedPlayers), Codec.LONG.listOf().lenientOptionalFieldOf("player_timers", List.of()).forGetter((data) -> data.playerTimers), Codec.LONG.lenientOptionalFieldOf("state_updating_resumes_at", 0L).forGetter((data) -> data.stateUpdatingResumesAt), ItemStack.CODEC.listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter((data) -> data.itemsToEject), Codec.INT.lenientOptionalFieldOf("total_ejections_needed", 0).forGetter((data) -> data.totalEjectionsNeeded)).apply(instance, TimerVaultServerData::new));
    public List<Long> playerTimers = new ArrayList<>();
    public boolean ominous = false;

    public TimerVaultServerData(Set<UUID> players, List<Long> timers, long stateUpdatingResumesAt, List<ItemStack> itemsToEject, int totalEjectionsNeeded) {
        UUID[] playerArray = players.toArray(new UUID[0]);
        Long[] timerArray = timers.toArray(new Long[0]);
        for (int i = 0; i < playerArray.length; i++) {
            rewardedPlayers.add(playerArray[i]);
            if (i < timerArray.length)
                playerTimers.add(timerArray[i]);
            else
                playerTimers.add(0L);
        }
        this.stateUpdatingResumesAt = stateUpdatingResumesAt;
        this.itemsToEject.addAll(itemsToEject);
        this.totalEjectionsNeeded = totalEjectionsNeeded;
    }

    public TimerVaultServerData() {
    }

    @Override
    public @NotNull Set<UUID> getRewardedPlayers() {
        return rewardedPlayers;
    }

    @Override
    public boolean hasRewardedPlayer(@NotNull Player player) {
        int i = 0;
        int result = -1;
        for (UUID uuid : rewardedPlayers) {
            if (uuid.equals(player.getUUID())) {
                result = i;
                break;
            }
            i++;
        }
        return result >= 0 && result < playerTimers.size() && player.level().getGameTime() < playerTimers.get(result);
    }

    public boolean hasRewardedPlayer(UUID uuid, long time) {
        int i = 0;
        int result = -1;
        for (UUID _uuid : rewardedPlayers) {
            if (_uuid.equals(uuid)) {
                result = i;
                break;
            }
            i++;
        }
        return result >= 0 && result < playerTimers.size() && time < playerTimers.get(result);
    }

    @Override
    public void addToRewardedPlayers(@NotNull Player player) {
        rewardedPlayers.add(player.getUUID());
        int i = 0;
        for (UUID uuid : rewardedPlayers) {
            if (uuid.equals(player.getUUID())) {
                long time = player.level().getGameTime() + (ominous ? Config.ominous_time : Config.normal_time);
                if (i < playerTimers.size())
                    playerTimers.set(i, time);
                else
                    playerTimers.add(time);
                break;
            }
            i++;
        }
        if (rewardedPlayers.size() > MAX_REWARD_PLAYERS) {
            int idxToRemove = -1;
            UUID uuidToRemove = null;
            int j = 0;
            for (UUID uuid : rewardedPlayers) {
                if (idxToRemove == -1 || playerTimers.get(j) < playerTimers.get(idxToRemove)) {
                    idxToRemove = j;
                    uuidToRemove = uuid;
                }
                j++;
            }
            playerTimers.remove(idxToRemove);
            rewardedPlayers.remove(uuidToRemove);
        }
        markChanged();
    }

    @Override
    public void set(VaultServerData other) {
        stateUpdatingResumesAt = other.stateUpdatingResumesAt();
        itemsToEject.clear();
        itemsToEject.addAll(other.itemsToEject);
        rewardedPlayers.clear();
        playerTimers.clear();
        if (other instanceof TimerVaultServerData data) {
            rewardedPlayers.addAll(data.rewardedPlayers);
            playerTimers.addAll(data.playerTimers);
            return;
        }
        for (UUID uuid : other.rewardedPlayers) {
            rewardedPlayers.add(uuid);
            playerTimers.add(0L);
        }
    }
}
package me.cloudsven.amethystapath.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CooldownUtil {
    // Structure: Map<AbilityName, Map<PlayerUUID, ExpirationTime>>
    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    /**
     * Sets a cooldown for a specific player and ability.
     * @param playerUUID The player UUID.
     * @param ability The name of the ability (e.g., "scarlet_haste").
     * @param seconds The duration in seconds.
     */

    public static void setCooldown(UUID playerUUID, String ability, int seconds) {
        long delay = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.computeIfAbsent(ability, k -> new HashMap<>()).put(playerUUID, delay);
    }

    /**
     * Checks if the player is still on cooldown.
     */
    public static boolean isOnCooldown(UUID playerUUID, String ability) {
        return getRemainingTime(playerUUID, ability) > 0;
    }

    /**
     * Returns remaining seconds, or 0 if the cooldown is over.
     */
    public static long getRemainingTime(UUID playerUUID, String ability) {
        Map<UUID, Long> abilityMap = cooldowns.get(ability);
        if (abilityMap == null || !abilityMap.containsKey(playerUUID)) {
            return 0;
        }

        long remaining = (abilityMap.get(playerUUID) - System.currentTimeMillis()) / 1000;
        return Math.max(remaining, 0);
    }
}
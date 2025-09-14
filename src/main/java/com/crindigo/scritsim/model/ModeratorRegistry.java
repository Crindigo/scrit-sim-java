package com.crindigo.scritsim.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModeratorRegistry {

    private static final Map<ModeratorInfo, IModeratorStats> MODERATORS = new HashMap<>();

    public static void registerModerator(@NotNull ItemStack stack, @NotNull IModeratorStats moderator) {
        String registry = stack.getId();
        MODERATORS.put(new ModeratorInfo(registry), moderator);
    }

    /*public static void registerModerator(@NotNull IBlockState state, @NotNull IModeratorStats moderator) {
        ResourceLocation registry = state.getBlock().getRegistryName();
        int meta = state.getBlock().getMetaFromState(state);
        MODERATORS.put(new ModeratorInfo(registry, meta), moderator);
    }

    @Nullable
    public static IModeratorStats getModerator(@NotNull IBlockState state) {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        return MODERATORS.get(new ModeratorInfo(block.getRegistryName(), meta));
    }*/

    public static Collection<ModeratorInfo> getAllModerators() {
        return MODERATORS.keySet();
    }

    public static class ModeratorInfo {

        @Getter
        private final String registryName;

        private ModeratorInfo(String registryName) {
            this.registryName = registryName;
        }

        @Override
        public int hashCode() {
            return registryName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ModeratorInfo && ((ModeratorInfo) obj).registryName.equals(registryName);
        }
    }
}

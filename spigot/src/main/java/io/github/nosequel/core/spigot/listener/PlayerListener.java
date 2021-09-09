package io.github.nosequel.core.spigot.listener;

import io.github.nosequel.core.shared.Pump;
import io.github.nosequel.core.shared.profile.PumpProfile;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.core.shared.rank.RankMetadata;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final Pump pump;

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        this.pump.getProfileHandler()
                .findOrFetch(event.getUniqueId(), event.getName())
                .join();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final Optional<PumpProfile> profileOptional = this.pump.getProfileHandler().find(player.getUniqueId());

        if (profileOptional.isPresent()) {
            final PumpProfile profile = profileOptional.get();
            final Rank rank = profile.getPrimaryGrant().getRank();

            final RankMetadata<?> prefixMetadata = rank.getMetadata().get("prefix");

            if (prefixMetadata != null) {
                event.setFormat(ChatColor.translateAlternateColorCodes('&', prefixMetadata.valueAsString() + "%s&7: &f") + "%s");
            }
        }
    }
}
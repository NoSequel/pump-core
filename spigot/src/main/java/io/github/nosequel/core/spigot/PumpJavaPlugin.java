package io.github.nosequel.core.spigot;

import io.github.nosequel.command.bukkit.BukkitCommandHandler;
import io.github.nosequel.core.shared.Pump;
import io.github.nosequel.core.shared.profile.PumpProfileHandler;
import io.github.nosequel.core.shared.rank.RankHandler;
import io.github.nosequel.core.spigot.listener.PlayerListener;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import io.github.nosequel.storage.mongo.settings.MongoSettings;
import io.github.nosequel.storage.mongo.settings.impl.NoAuthMongoSettings;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PumpJavaPlugin extends JavaPlugin {

    private Pump pump;

    @Override
    public void onEnable() {
        final MongoSettings settings = new NoAuthMongoSettings("127.0.0.1", 27017, "pump");
        final MongoStorageHandler storageHandler = new MongoStorageHandler(settings);

        this.pump = Pump.builder()
                .storageHandler(storageHandler)
                .rankHandler(new RankHandler(storageHandler))
                .profileHandler(new PumpProfileHandler(storageHandler))
                .commandHandler(new BukkitCommandHandler("pump"))
                .build();

        this.pump.load();

        // register listeners
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this.pump), this);
    }

    @Override
    public void onDisable() {
        this.pump.unload();
    }
}
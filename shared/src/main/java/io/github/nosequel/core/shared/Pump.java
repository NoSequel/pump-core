package io.github.nosequel.core.shared;

import io.github.nosequel.command.CommandHandler;
import io.github.nosequel.core.shared.command.RankCommand;
import io.github.nosequel.core.shared.command.adapter.RankTypeAdapter;
import io.github.nosequel.core.shared.profile.PumpProfileHandler;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.core.shared.rank.RankHandler;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pump {

    private final MongoStorageHandler storageHandler;
    private final RankHandler rankHandler;
    private final PumpProfileHandler profileHandler;

    // the handler for registering/handling commands
    private final CommandHandler commandHandler;

    public void load() {
        this.rankHandler.load();

        if (this.commandHandler != null) {
            this.commandHandler.registerTypeAdapter(Rank.class, new RankTypeAdapter(this.rankHandler));
            this.commandHandler.registerCommand(new RankCommand(this.rankHandler));
        }
    }

    public void unload() {
        this.rankHandler.save();
        this.profileHandler.save();
    }

}
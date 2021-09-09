package io.github.nosequel.core.shared.command;

import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Subcommand;
import io.github.nosequel.command.executor.CommandExecutor;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.core.shared.rank.RankHandler;
import io.github.nosequel.core.shared.rank.RankMetadata;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class RankCommand {

    private final RankHandler rankHandler;

    @Command(label = "rank", permission = "rank.help", userOnly = false)
    public void execute(CommandExecutor executor) {
        executor.sendMessage("&c/rank create <name>");
    }

    @Subcommand(label = "create", parentLabel = "rank", permission = "rank.create", userOnly = false)
    public void createRank(CommandExecutor executor, String name) {
        if (this.rankHandler.find(name).isPresent()) {
            executor.sendMessage("&cA rank with that name already exists.");
            return;
        }

        this.rankHandler.register(new Rank(UUID.randomUUID(), name));
        executor.sendMessage("&eYou have &acreated &ethe &d" + name + " &erank.");
    }

    @Subcommand(label = "prefix", parentLabel = "rank", permission = "rank.prefix", userOnly = false)
    public void updatePrefix(CommandExecutor executor, Rank rank, String prefix) {
        this.handleMetadata(executor, rank, prefix, "prefix");
    }

    @Subcommand(label = "suffix", parentLabel = "rank", permission = "rank.prefix", userOnly = false)
    public void updateSuffix(CommandExecutor executor, Rank rank, String suffix) {
        this.handleMetadata(executor, rank, suffix, "suffix");
    }

    @Subcommand(label = "weight", parentLabel = "rank", permission = "rank.prefix", userOnly = false)
    public void updateWeight(CommandExecutor executor, Rank rank, Integer weight) {
        this.handleMetadata(executor, rank, weight, "weight");
    }

    @Subcommand(label = "debug", parentLabel = "rank", permission = "rank.debug", userOnly = false)
    public void handleDebug(CommandExecutor executor) {
        for (Rank rank : this.rankHandler.getRanks()) {
            executor.sendMessage(" - " + rank.getName());

            for (Map.Entry<String, RankMetadata<?>> entry : rank.getMetadata().entrySet()) {
                executor.sendMessage("  |  " + entry.getKey() + ", " + entry.getValue().getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void handleMetadata(CommandExecutor executor, Rank rank, T value, String key) {
        final RankMetadata<T> metadata = (RankMetadata<T>) rank.getMetadata().get(key);

        if (metadata != null) {
            metadata.setValue(value);
        }

        executor.sendMessage("&eYou have set the &d" + key + " &eof the &d" + rank.getName() + " &erank to &d" + value);
    }
}
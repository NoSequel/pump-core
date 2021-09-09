package io.github.nosequel.core.shared.rank;

import com.google.gson.Gson;
import io.github.nosequel.core.shared.PumpConstants;
import io.github.nosequel.core.shared.profile.adapter.RankTypeAdapter;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import io.github.nosequel.storage.mongo.provider.MongoStorageProvider;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

@Getter
public class RankHandler extends MongoStorageProvider<Rank> {

    private final Set<Rank> ranks = new HashSet<>();

    public RankHandler(MongoStorageHandler storageHandler) {
        super("ranks", storageHandler, Rank.class, new Gson());

        PumpConstants.RANK_TYPE_ADAPTER = new RankTypeAdapter(this);
    }

    public void load() {
        // load all ranks from the database
        // don't call async, should only be called on startup.
        this.fetchAllEntries()
                .join()
                .forEach((keys, ranks) -> this.register(ranks));

        // load the default ranks in case it's required
        this.loadDefaults();
    }

    public void save() {
        for (Rank rank : this.ranks) {
            this.setEntry(rank.getUniqueId().toString(), rank);
        }
    }

    /**
     * Load all of the registered default ranks.
     * <p>
     * This loops through all ranks within the {@link PumpConstants#DEFAULT_RANKS}
     * set and registers them if the rank could
     * not be found within the rank handler ranks collection.
     */
    public void loadDefaults() {
        System.out.println(this.ranks);

        for (Rank rank : PumpConstants.DEFAULT_RANKS) {
            if (!this.find(rank.getName()).isPresent()) {
                this.register(rank);
            }
        }
    }

    /**
     * Find a {@link Rank} object within the {@link RankHandler#ranks} collection
     * by the rank's unique identifier.
     *
     * @param uniqueId the name to find the rank by
     * @return the optional of the rank
     */
    public Optional<Rank> find(UUID uniqueId) {
        return this.find(rank -> rank.getUniqueId().equals(uniqueId));
    }

    /**
     * Find a {@link Rank} object within the {@link RankHandler#ranks} collection
     * by the rank's name identifier.
     *
     * @param name the name to find the rank by
     * @return the optional of the rank
     */
    public Optional<Rank> find(String name) {
        return this.find(rank -> rank.getName().equalsIgnoreCase(name));
    }

    /**
     * Find a {@link Rank} object within the {@link RankHandler#ranks} collection
     * using the provided predicate within the method.
     *
     * @param predicate the predicate to use to filter through the ranks
     * @return the optional of the rank
     */
    public Optional<Rank> find(Predicate<? super Rank> predicate) {
        return this.ranks.stream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Register a new rank to the {@link RankHandler#ranks} collection.
     * <p>
     * This method automatically checks if the
     * rank contains all of the default rank metadata,
     * if it doesn't it will automatically add it,
     *
     * @param rank the rank to register
     */
    public void register(Rank rank) {
        this.ranks.add(rank);

        for (Map.Entry<String, RankMetadata<?>> entry : PumpConstants.DEFAULT_RANK_METADATA.entrySet()) {
            if (!rank.getMetadata().containsKey(entry.getKey())) {
                rank.getMetadata().put(entry.getKey(), entry.getValue());
            }
        }
    }
}
package io.github.nosequel.core.shared.rank;

import com.google.gson.Gson;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import io.github.nosequel.storage.mongo.provider.MongoStorageProvider;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

@Getter
public class RankHandler extends MongoStorageProvider<Rank> {

    private final Set<Rank> ranks = new HashSet<>();

    public static Map<String, RankMetadata<?>> DEFAULT_RANK_METADATA = new HashMap<String, RankMetadata<?>>() {{
        put("prefix", new RankMetadata<>(""));
        put("suffix", new RankMetadata<>(""));
        put("weight", new RankMetadata<>(0));
    }};

    public RankHandler(MongoStorageHandler storageHandler) {
        super("ranks", storageHandler, Rank.class, new Gson());
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
        for (Map.Entry<String, RankMetadata<?>> entry : DEFAULT_RANK_METADATA.entrySet()) {
            if (!rank.getMetadata().containsKey(entry.getKey())) {
                rank.getMetadata().put(entry.getKey(), entry.getValue());
            }
        }

        this.ranks.add(rank);
    }
}
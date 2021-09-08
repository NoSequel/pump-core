package io.github.nosequel.core.shared.rank;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Rank {

    private final UUID uniqueId;
    private final String name;

    private final Map<String, RankMetadata<?>> metadata = new HashMap<>();

    /**
     * Constructor to make a new Rank object.
     *
     * @param uniqueId the unique identifier of the rank to identify the rank with
     * @param name     the name identifier of the rank
     */
    public Rank(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        for (Map.Entry<String, RankMetadata<?>> entry : RankHandler.DEFAULT_RANK_METADATA.entrySet()) {
            this.metadata.put(entry.getKey(), entry.getValue());
        }
    }
}
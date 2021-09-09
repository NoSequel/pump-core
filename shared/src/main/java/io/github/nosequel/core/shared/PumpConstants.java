package io.github.nosequel.core.shared;

import io.github.nosequel.core.shared.profile.adapter.RankTypeAdapter;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.core.shared.rank.RankMetadata;

import java.util.*;

public class PumpConstants {

    public static RankTypeAdapter RANK_TYPE_ADAPTER;

    public static final UUID CONSOLE_UUID = UUID.fromString("a64a9ac8-c617-4b7a-8c4b-42d81cb3e6b4");

    public static Map<String, RankMetadata<?>> DEFAULT_RANK_METADATA = new HashMap<String, RankMetadata<?>>() {{
        put("prefix", new RankMetadata<>(""));
        put("suffix", new RankMetadata<>(""));
        put("weight", new RankMetadata<>(0));
    }};

    public static final Set<Rank> DEFAULT_RANKS = new HashSet<>(Arrays.asList(
        new Rank(UUID.fromString("a64a9ac8-c617-4b7a-8c4b-42d81cb3e6b4"), "Default")
    ));

}

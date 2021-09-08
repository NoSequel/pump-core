package io.github.nosequel.core.shared;

import io.github.nosequel.core.shared.rank.RankHandler;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pump {

    private final MongoStorageHandler storageHandler;
    private final RankHandler rankHandler;

}
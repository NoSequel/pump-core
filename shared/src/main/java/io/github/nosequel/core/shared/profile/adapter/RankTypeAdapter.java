package io.github.nosequel.core.shared.profile.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.core.shared.rank.RankHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class RankTypeAdapter extends TypeAdapter<Rank> {

    private final RankHandler rankHandler;

    @Override
    public void write(JsonWriter jsonWriter, Rank rank) throws IOException {
        jsonWriter.value(rank.getUniqueId().toString());
    }

    @Override
    public Rank read(JsonReader jsonReader) throws IOException {
        return this.rankHandler
                .find(UUID.fromString(jsonReader.nextString()))
                .orElse(null);
    }
}
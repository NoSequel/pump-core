package io.github.nosequel.core.shared.command.adapter;

import io.github.nosequel.command.adapter.TypeAdapter;
import io.github.nosequel.command.executor.CommandExecutor;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.core.shared.rank.RankHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RankTypeAdapter implements TypeAdapter<Rank> {

    private final RankHandler rankHandler;

    @Override
    public Rank convert(CommandExecutor commandExecutor, String s) throws Exception {
        return this.rankHandler.find(s).orElseThrow(() -> new NullPointerException("Unable to find rank by that name."));
    }
}
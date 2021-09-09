package io.github.nosequel.core.shared.grant;

import io.github.nosequel.core.shared.rank.Rank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Grant {

    private final Rank rank;

    private final UUID target;
    private final UUID executor;

    private final long startTime = System.currentTimeMillis();

    private long duration = -1L;
    private boolean expired;

    public boolean hasExpired() {
        return (this.expired = this.expired || (this.duration != -1L && (this.duration + this.startTime) - System.currentTimeMillis() < 0)); }

}
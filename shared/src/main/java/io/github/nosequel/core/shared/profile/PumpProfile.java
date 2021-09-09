package io.github.nosequel.core.shared.profile;

import io.github.nosequel.core.shared.PumpConstants;
import io.github.nosequel.core.shared.grant.Grant;
import io.github.nosequel.core.shared.rank.Rank;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class PumpProfile {

    private final UUID uniqueId;
    private final String name;

    private final Set<Grant> grants = new HashSet<>();

    /**
     * Constructor to create a new {@link PumpProfile} object.
     *
     * @param uniqueId the unique identifier of the player
     * @param name     the name of the player
     */
    public PumpProfile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        for (Rank rank : PumpConstants.DEFAULT_RANKS) {
            this.grants.add(Grant.builder()
                    .executor(UUID.randomUUID())
                    .target(this.uniqueId)
                    .rank(rank).build()
            );
        }
    }

    public Grant getPrimaryGrant() {
        for (Grant grant : this.grants) {
            if (!grant.isExpired()) {
                return grant;
            }
        }

        for (Rank rank : PumpConstants.DEFAULT_RANKS) {
            this.grants.add(Grant.builder()
                    .executor(PumpConstants.CONSOLE_UUID)
                    .target(this.uniqueId)
                    .rank(rank).build()
            );
        }

        return this.getPrimaryGrant();
    }
}
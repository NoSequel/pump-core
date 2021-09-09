package io.github.nosequel.core.shared.profile;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import io.github.nosequel.core.shared.PumpConstants;
import io.github.nosequel.core.shared.rank.Rank;
import io.github.nosequel.storage.mongo.MongoStorageHandler;
import io.github.nosequel.storage.mongo.provider.MongoStorageProvider;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class PumpProfileHandler extends MongoStorageProvider<PumpProfile> {

    private final Set<PumpProfile> profiles = new HashSet<>();

    public PumpProfileHandler(MongoStorageHandler storageHandler) {
        super("profiles", storageHandler, PumpProfile.class, new GsonBuilder()
                .setPrettyPrinting()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapter(Rank.class, PumpConstants.RANK_TYPE_ADAPTER)
                .create()
        );
    }

    public void save() {
        for (PumpProfile profile : this.profiles) {
            this.setEntry(profile.getUniqueId().toString(), profile);
        }
    }

    /**
     * Register a new profile to the profiles collection
     *
     * @param profile the profile to register
     */
    public void register(PumpProfile profile) {
        this.profiles.add(profile);
    }

    /**
     * Find a {@link PumpProfile} object within the {@link PumpProfileHandler#profiles} collection
     * or fetch it from the database.
     *
     * @param uniqueId the unique identifier to find the profile by
     * @return the found profile as a completable future
     */
    public CompletableFuture<PumpProfile> findOrFetch(UUID uniqueId, String name) {
        return CompletableFuture.supplyAsync(() -> this.find(uniqueId).orElseGet(() -> {
            final PumpProfile profile = this.fetchEntry(uniqueId.toString()).join();

            if (profile == null) {
                this.register(new PumpProfile(uniqueId, name));
            } else {
                this.register(profile);
            }

            return this.find(uniqueId).orElseGet(() -> this.find(name).orElse(null));
        }));
    }

    /**
     * Find a {@link PumpProfile} object within the {@link PumpProfileHandler#profiles} collection
     * by the Profile's unique identifier.
     *
     * @param uniqueId the name to find the Profile by
     * @return the optional of the Profile
     */
    public Optional<PumpProfile> find(UUID uniqueId) {
        return this.find(profiles -> profiles.getUniqueId().equals(uniqueId));
    }

    /**
     * Find a {@link PumpProfile} object within the {@link PumpProfileHandler#profiles} collection
     * by the Profile's name identifier.
     *
     * @param name the name to find the Profile by
     * @return the optional of the Profile
     */
    public Optional<PumpProfile> find(String name) {
        return this.find(profiles -> profiles.getName().equalsIgnoreCase(name));
    }

    /**
     * Find a {@link PumpProfile} object within the {@link PumpProfileHandler#profiles} collection
     * using the provided predicate within the method.
     *
     * @param predicate the predicate to use to filter through the Profiles
     * @return the optional of the Profile
     */
    public Optional<PumpProfile> find(Predicate<? super PumpProfile> predicate) {
        return this.profiles.stream()
                .filter(predicate)
                .findFirst();
    }
}
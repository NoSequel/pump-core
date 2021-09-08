package io.github.nosequel.core.shared.rank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankMetadata<T> {

    private final T value;

    public String valueAsString() {
        return (String) value;
    }

    public int valueAsInteger() {
        return (int) value;
    }

    public boolean valueAsBoolean() {
        return (boolean) value;
    }
}
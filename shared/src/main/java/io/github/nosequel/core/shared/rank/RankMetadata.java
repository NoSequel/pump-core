package io.github.nosequel.core.shared.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RankMetadata<T> {

    private T value;

    public String valueAsString() {
        return (String) value;
    }

    public int valueAsInteger() {
        return (Integer) value;
    }

    public boolean valueAsBoolean() {
        return (Boolean) value;
    }
}
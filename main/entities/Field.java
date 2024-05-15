package entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Field {
    private final int x;
    private final int y;

    @Override
    public String toString() {
        return String.format("Поле (%d:%d)", x, y);
    }
}

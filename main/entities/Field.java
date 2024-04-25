package entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Field {
    private final int x;
    private final int y;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

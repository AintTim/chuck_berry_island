package entities;

import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Entity {
    @Setter(AccessLevel.PROTECTED)
    protected Long id;
    protected Double weight;
    protected Integer velocity;
    protected Integer limit;
    protected Double hunger;
    @Setter
    protected Boolean removable;

    protected Entity(Double weight, Integer velocity, Integer limit, Double hunger) {
        this.weight = weight;
        this.velocity = velocity;
        this.limit = limit;
        this.hunger = hunger;
        this.removable = false;
    }
}

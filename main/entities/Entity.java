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
    @Setter
    protected String picture;

    protected Entity(Double weight, Integer velocity, Integer limit, Double hunger, String picture) {
        this.weight = weight;
        this.velocity = velocity;
        this.limit = limit;
        this.hunger = hunger;
        this.picture = picture;
        this.removable = false;
    }
}

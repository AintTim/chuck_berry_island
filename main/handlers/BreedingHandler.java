package handlers;

import constants.Action;
import constants.EntityType;
import entities.Animal;
import entities.Island;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class BreedingHandler {
    private final Island island;

    public Animal getRandomBreedingPartner(Animal animal) {
        var location = island.locateEntity(animal);
        var type = EntityType.ofClass(animal.getClass());
        var partners = island.getFields().get(location).get(type).stream()
                .map(Animal.class::cast)
                .filter(partner -> isSuitablePartner(animal, partner))
                .toList();

        return partners.isEmpty()
                ? null
                : partners.get(ThreadLocalRandom.current().nextInt(partners.size()));
    }

    private boolean isSuitablePartner(Animal animal, Animal partner) {
        return Action.BREED.equals(partner.getAction()) && !partner.isActionDone() && !partner.equals(animal);
    }
}

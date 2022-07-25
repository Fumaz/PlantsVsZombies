package dev.fumaz.plantsvszombies.level;

import dev.fumaz.plantsvszombies.yard.Yard;

public enum Level {
    ONE(LevelOne.class),
    TWO(LevelTwo.class);

    private final Class<? extends AbstractLevel> levelClass;

    Level(Class<? extends AbstractLevel> levelClass) {
        this.levelClass = levelClass;
    }

    public AbstractLevel create(Yard yard) {
        try {
            return levelClass.getConstructor(Yard.class).newInstance(yard);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

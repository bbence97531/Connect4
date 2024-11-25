package hu.nye.model;

import java.util.Objects;

public class Player {
    private final String name;
    private final char disc;

    public Player(String name, char disc) {
        this.name = name;
        this.disc = disc;
    }

    public String getName() {
        return name;
    }

    public char getDisc() {
        return disc;
    }

    //Value Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return disc == player.disc && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, disc);
    }
}


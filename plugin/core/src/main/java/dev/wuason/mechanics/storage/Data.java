package dev.wuason.mechanics.storage;

import java.io.Serializable;

public abstract class Data implements Serializable {

    private final String id;

    public Data(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}

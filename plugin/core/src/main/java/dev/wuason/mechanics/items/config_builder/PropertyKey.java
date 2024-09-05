package dev.wuason.mechanics.items.config_builder;

public interface PropertyKey {
    String getKey();
    String getId();

    static PropertyKey of(String key, String id) {
        return new PropertyKey() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getId() {
                return id;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                PropertyKey that = (PropertyKey) obj;
                return getKey().equals(that.getKey()) && getId().equals(that.getId());
            }
        };
    }
}

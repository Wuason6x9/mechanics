package dev.wuason.mechanics.library.dependencies;

import java.util.*;

public class Dependencies {

    public final static Map<String, Dependency> DEPENDENCIES_MAP = new HashMap<>();

    public static final Dependency BOOSTED_YAML = new Dependency("dev:dejvokep", "boosted-yaml", "1.3.6",
            Remap.of("dev:dejvokep:boostedyaml", "dev:wuason:libs:boostedyaml")
    );

    public static final Dependency BEAN_SHELL = new Dependency("org:apache-extras:beanshell", Texts.BEAN_SHELL, "2.1.1",
            Remap.of(Texts.BEAN_SHELL, "dev:wuason:libs:" + Texts.BEAN_SHELL)
    );

    public static final Dependency COMMAND_API = new Dependency("dev:jorel", "commandapi-bukkit-shade", "9.7.0",
            Remap.of("dev:jorel:commandapi", "dev:wuason:libs:commandapi")
    );

    public static final Dependency COMMAND_API_MOJANG_MAPPED = new Dependency("dev:jorel", "commandapi-bukkit-shade-mojang-mapped", "9.7.0",
            Remap.of("dev:jorel:commandapi", "dev:wuason:libs:commandapi")
    );

    public static final Dependency NBT_API = new Dependency("de:tr7zw", "item-nbt-api", "2.14.1",
            Remap.of("de:tr7zw:changeme:nbtapi", "dev:wuason:libs:nbtapi")
    );

    public static final Dependency PROTECTION_LIB = new Dependency("com:github:oraxen", "protectionlib", "1.5.1",
            Remap.of("io:th0rgal:protectionlib", "dev:wuason:libs:protectionlib")
    );

    public static final Dependency ASM = new Dependency("org:ow2:asm", "asm", "9.7",
            Remap.of("org:ow2:asm", "dev:wuason:libs:asm")
    );

    public static final Dependency ASM_COMMONS = new Dependency("org:ow2:asm", "asm-commons", "9.7",
            Remap.of("org:ow2:asm", "dev:wuason:libs:asm")
    );

    public static final Dependency APACHE_COMMONS = new Dependency("org:apache:commons", "commons-lang3", "3.14.0",
            Remap.of("org:apache", "dev:wuason:libs:apache")
    );

    public static final Dependency GSON = new Dependency("com:google:code:gson", "gson", "2.11.0",
            Remap.of("com:google:gson", "dev:wuason:libs:google:gson")
    );

    public static final Dependency GOOGLE_ERROR_PRONE_ANNOTATIONS = new Dependency("com:google:errorprone", "error_prone_annotations", "2.30.0",
            Remap.of("com:google:errorprone", "dev:wuason:libs:google:errorprone")
    );

    public static final Dependency MORE_PERSISTENT_DATA_TYPES = new Dependency("com:jeff-media", "MorePersistentDataTypes", "2.4.0",
            Remap.of("com:jeff_media:morepersistentdatatypes", "dev:wuason:libs:jeffmedia:morepersistentdatatypes")
    );

    public static final Dependency CUSTOM_BLOCK_DATA = new Dependency("com:jeff-media", "custom-block-data", "2.2.2",
            Remap.of("com:jeff_media:customblockdata", "dev:wuason:libs:jeffmedia:customblockdata")
    );

    public static final Dependency ADAPTER = new Dependency("com:github:Wuason6x9", "Adapter", "1.0",
            Remap.of("dev:wuason:adapter", "dev:wuason:libs:adapter")
    );



    static {
        addDependency(
                BOOSTED_YAML,
                BEAN_SHELL,
                COMMAND_API,
                COMMAND_API_MOJANG_MAPPED,
                NBT_API,
                PROTECTION_LIB,
                ASM,
                ASM_COMMONS,
                APACHE_COMMONS,
                GSON,
                GOOGLE_ERROR_PRONE_ANNOTATIONS,
                MORE_PERSISTENT_DATA_TYPES,
                CUSTOM_BLOCK_DATA,
                ADAPTER
        );
    }

    public static void addDependency(Dependency... dependency) {
        for (Dependency dep : dependency) {
            DEPENDENCIES_MAP.put(dep.toString(), dep);
        }
    }

    public static void removeDependency(Dependency dependency) {
        DEPENDENCIES_MAP.remove(dependency.toString());
    }

    public static void clearDependencies() {
        DEPENDENCIES_MAP.clear();
    }

    public static Dependency getDependency(String mavenFormat) { // dev:dejvokep:boosted-yaml:1.3.6
        return DEPENDENCIES_MAP.get(mavenFormat);
    }

    public static Optional<Dependency> getDependencyOptional(String mavenFormat) {
        return Optional.ofNullable(DEPENDENCIES_MAP.get(mavenFormat));
    }

    public static Dependency createDependency(String groupId, String artifactId, String version) {
        Dependency dependency = new Dependency(groupId, artifactId, version);
        DEPENDENCIES_MAP.put(dependency.toString(), dependency);
        return dependency;
    }
}

package dev.wuason.mechanics.library.dependencies;

public class Dependencies {
    public static final Dependency BOOSTED_YAML = new Dependency("dev:dejvokep", "boosted-yaml", "1.3.6",
            Remap.of("dev:dejvokep:boostedyaml", "dev:wuason:libs:boostedyaml")
    );

    public static final Dependency BEAN_SHELL = new Dependency("org:apache-extras:beanshell", Texts.BEAN_SHELL, "2.1.1",
            Remap.of(Texts.BEAN_SHELL, "dev:wuason:libs:" + Texts.BEAN_SHELL)
    );

    public static final Dependency COMMAND_API = new Dependency("dev:jorel", "commandapi-bukkit-shade", "9.5.1",
            Remap.of("dev:jorel:commandapi", "dev:wuason:libs:commandapi")
    );

    public static final Dependency COMMAND_API_MOJANG_MAPPED = new Dependency("dev:jorel", "commandapi-bukkit-shade-mojang-mapped", "9.5.1",
            Remap.of("dev:jorel:commandapi", "dev:wuason:libs:commandapi")
    );

    public static final Dependency NBT_API = new Dependency("de:tr7zw", "item-nbt-api", "2.13.1",
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
}

# MECHANICS

![bStats Players](https://img.shields.io/bstats/players/23026) ![bStats Servers](https://img.shields.io/bstats/servers/23026)

### WIKI for user [Link](https://wiki.techmc.es/en/mechanics)

### [JavaDoc](https://javadoc.jitpack.io/com/github/Wuason6x9/mechanics/1.0.2/javadoc/)

# Libraries

- Boosted-Yaml [Link](https://github.com/dejvokep/boosted-yaml) (v1.3.6)
- commons-lang3 [Link](https://commons.apache.org/proper/commons-lang/) (v3.14.0)
- Item-NBT-API [Link](https://github.com/tr7zw/Item-NBT-API) (v2.13.1)
- BSH Interpreter [Link](https://github.com/beanshell/beanshell) (v2.1.1)
- ProtectionLib [Link](https://github.com/oraxen/protectionlib) (v1.5.1)
- CommandAPI [Link](https://github.com/JorelAli/CommandAPI) (v9.5.3)
- Google GSON [Link](https://github.com/google/gson) (v2.11.0)
- More Persistent Data Types [Link](https://github.com/mfnalex/MorePersistentDataTypes) (v2.4.0)
- Custom block Data [Link](https://github.com/mfnalex/CustomBlockData) (v2.2.2)
- RTAG [Link](https://github.com/saicone/rtag) (v1.5.5)


## Add as dependency

### MAVEN (**.pom**)

Add the repository to your pom.xml file:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency:
```xml
<dependency>
    <groupId>com.github.Wuason6x9</groupId>
    <artifactId>mechanics</artifactId>
    <version>RELEASE-VERSION</version>
    <scope>provided</scope>
</dependency>
```

### GRADLE (**build.gradle**)

Add the repository to your build.gradle file:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add the dependency:
```gradle

dependencies {
    compileOnly 'com.github.Wuason6x9:mechanics:RELEASE-VERSION'
}
```

### GRADLE KOTLIN DSL (**build.gradle.kts**)

Add the repository to your build.gradle.kts file:
```kotlin
repositories {
    maven("https://jitpack.io")
}
```

Add the dependency:
```kotlin
dependencies {
    compileOnly("com.github.Wuason6x9:mechanics:RELEASE-VERSION")
}
```

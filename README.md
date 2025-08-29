# MECHANICS

## Add as dependency

### MAVEN (**.pom**)

Add the repository to your pom.xml file:
```xml
<repositories>
    <repository>
        <id>techmc-studios-releases</id>
        <name>TechMC Repository</name>
        <url>https://repo.techmc.es/releases</url>
    </repository>
</repositories>
```

Add the dependency:
```xml
<dependency>
    <groupId>dev.wuason</groupId>
    <artifactId>mechanics</artifactId>
    <version>RELEASE</version>
    <scope>provided</scope>
</dependency>
```

### GRADLE GROOVY (**build.gradle**)

Add the repository to your build.gradle file:
```gradle
repositories {
    maven {
        name "techmcStudiosReleases"
        url "https://repo.techmc.es/releases"
    }
}
```

Add the dependency:
```gradle

dependencies {
    compileOnly 'dev.wuason:mechanics:RELEASE'
}
```

### GRADLE KOTLIN DSL (**build.gradle.kts**)

Add the repository to your build.gradle.kts file:
```kotlin
repositories {
    maven {
        name = "techmcStudiosReleases"
        url = uri("https://repo.techmc.es/releases")
    }
}
```

Add the dependency:
```kotlin
dependencies {
    compileOnly("dev.wuason:mechanics:RELEASE")
}
```

### WIKI for user [Link](https://docs.techmc.es/en/mechanics)
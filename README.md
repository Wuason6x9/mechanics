# MECHANICS

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
    <scope>compile</scope>
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

### WIKI for user [Link](https://wiki.techmc.es/en/mechanics)
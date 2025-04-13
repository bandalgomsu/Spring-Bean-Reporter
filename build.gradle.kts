import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
    signing
    id("com.vanniktech.maven.publish") version "0.29.0"
}

group = "io.github.bandalgomsu"
version = "1.0.0"

tasks.bootJar { enabled = false }
tasks.jar { enabled = true }

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

mavenPublishing {
    coordinates( // Coordinate(GAV)
        groupId = "io.github.bandalgomsu",
        artifactId = "spring-bean-reporter",
        version = "1.0.1"
    )

    pom {
        name.set("spring-bean-reporter") // Project Name
        description.set("Spring Bean Info Reporter") // Project Description
        inceptionYear.set("2025") // 개시년도
        url.set("https://github.com/bandalgomsu/Spring-Bean-Reporter") // Project URL

        licenses { // License Information
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers { // Developer Information
            developer {
                id.set("gomsu")
                name.set("gomsu")
                email.set("rhtn1128@gmail.com")
            }
        }

        scm { // SCM Information
            connection.set("scm:git:git://github.com/bandalgomsu/Spring-Bean-Reporter.git")
            developerConnection.set("scm:git:ssh://github.com/bandalgomsu/Spring-Bean-Reporter.git")
            url.set("https://github.com/bandalgomsu/Spring-Bean-Reporter")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications() // GPG/PGP 서명
}

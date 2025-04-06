plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    `maven-publish`
}

group = "com"
version = "1.0.0"

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
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


publishing {
    publications {
        create<MavenPublication>("Spring-Bean-Reporter") {
            from(components["java"])

            groupId = "com.spring-bean-reporter"
            artifactId = "spring-bean-reporter"
            version = "1.0.0"

            pom {
                name.set("Spring-Bean-Reporter")
                description.set("Spring Bean Info Reporter")
                url.set("https://github.com/bandalgomsu/Spring-Bean-Reporter.git")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("gomsu")
                        name.set("gomsu")
                        email.set("rhtn1128@gmail.com")
                    }
                }

                scm {
                    url.set("https://github.com/bandalgomsu/Spring-Bean-Reporter")
                    connection.set("scm:git:https://github.com/bandalgomsu/Spring-Bean-Reporter")
                }
            }
        }
    }

    repositories {
        // Maven Central, OSSRH, GitHub Packages 등
        // Maven Central
        maven {
            name = "sonatype"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

val querydslVersion by project.properties

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.10.0")
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")
    implementation("com.querydsl:querydsl-sql:$querydslVersion")
    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
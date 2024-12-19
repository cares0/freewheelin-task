plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.jpa")
	kotlin("kapt")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	id("org.asciidoctor.jvm.convert")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

val snippetsDir = file("build/generated-snippets").also { extra["snippetsDir"] = it }
val asciidoctorExtensions: Configuration by configurations.creating
val querydslVersion by project.properties

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.10.0")

	implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
	implementation("com.querydsl:querydsl-sql:$querydslVersion")
	kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

noArg {
	annotation("freewheelin.common.annotation.DefaultConstructor")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(snippetsDir)
}

tasks.asciidoctor {
	configurations(asciidoctorExtensions.name)
	inputs.dir(snippetsDir)
	dependsOn(tasks.test)
	sources {
		include("**/index.adoc", "**/popup/*.adoc")
	}

	baseDirFollowsSourceFile()

	doFirst {
		delete(file("src/main/resources/static/docs"))
	}
}

tasks.register<Copy>("copyDocument").configure {
	dependsOn(tasks.asciidoctor)
	from(file("build/docs/asciidoc"))
	into(file("src/main/resources/static/docs"))
}

tasks.register<Copy>("buildDocument") {
	dependsOn("copyDocument")
	from(file("src/main/resources/static/docs"))
	into(file("build/resources/main/static/docs"))
}

tasks.resolveMainClassName.configure {
	dependsOn(tasks.named("buildDocument"))
}

tasks.bootJar {
	dependsOn("buildDocument")
}
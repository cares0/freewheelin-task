plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.jpa")
	kotlin("kapt")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	id("org.asciidoctor.jvm.convert")
	id("com.google.devtools.ksp")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

val snippetsDir = file("build/generated-snippets").also { extra["snippetsDir"] = it }
val asciidoctorExtensions: Configuration by configurations.creating
val querydslVersion by project.properties
val kdslVersion = "1.0.4"

dependencies {
	implementation(project(":common"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.10.0")

//	implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
//	implementation("com.querydsl:querydsl-sql:$querydslVersion")
	implementation("io.github.openfeign.querydsl:querydsl-jpa:$querydslVersion")
	kapt("io.github.openfeign.querydsl:querydsl-apt:$querydslVersion:jpa")
//	kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

	implementation("io.github.cares0:rest-docs-kdsl-ksp:$kdslVersion")
	ksp("io.github.cares0:rest-docs-kdsl-ksp:$kdslVersion")
	implementation("io.github.millij:poi-object-mapper:1.0.0")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.mockk:mockk:1.13.14")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
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
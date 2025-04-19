plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.5.0"
	id("io.gitlab.arturbosch.detekt") version ("1.23.6")
}

group = "ru.walking"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

detekt {
	toolVersion = "1.23.6"
	config.setFrom("${project.rootDir}/data/detekt/detekt.yml")
	autoCorrect = true
	parallel = true
	source.setFrom(
		project.files(
			"src/main/java",
			"src/main/kotlin"
		)
	)
}

configurations.all {
	resolutionStrategy.eachDependency {
		if (requested.group == "org.jetbrains.kotlin") {
			useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
		}
	}
}

openApiGenerate {
	generatorName = "kotlin-spring"
	inputSpec = "$rootDir/data/specs/api.yml"
	outputDir = "$buildDir/generated"

	apiPackage = "ru.walking.api"
	modelPackage = "ru.walking.model"

	configOptions = mapOf(
		"interfaceOnly" to "true",
		"useTags" to "true",
		"useBeanValidation" to "false",
		"generateUnusedSchemas" to "true",
		"documentationProvider" to "none",
		"serializationLibrary" to "jackson",
		"useSpringBoot3" to "true",
		"skipDefaultInterface" to "false",
		"generateApiDocumentation" to "false",
		"generateApiTests" to "false",
		"generateModelTests" to "false",
		"generateModelDocumentation" to "false",
		"generateSupportingFiles" to "false"
	)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	dependsOn(tasks.openApiGenerate)
	kotlinOptions {
		jvmTarget = "17"
	}
}

sourceSets.main {
	java.srcDirs("$buildDir/generated/src/main/kotlin")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.session:spring-session-core")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("net.logstash.logback:logstash-logback-encoder:7.4")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

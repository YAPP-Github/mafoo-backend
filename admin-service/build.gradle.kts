plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	id("com.google.cloud.tools.jib") version "3.4.2"
}

group = "kr.mafoo"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	//implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib {
	val activeProfile: String? = System.getenv("SPRING_PROFILES_ACTIVE")
	val imageName: String? = System.getenv("IMAGE_NAME")
	val imageTag: String? = System.getenv("IMAGE_TAG")
	val serverPort: String = System.getenv("SERVER_PORT") ?: "8080"
	from {
		image = "amazoncorretto:17-alpine3.17-jdk"
	}
	to {
		image = imageName
		tags = setOf(imageTag, "latest")
	}
	container {
		jvmFlags =
			listOf(
				"-Dspring.profiles.active=$activeProfile",
				"-Dserver.port=$serverPort",
				"-Djava.security.egd=file:/dev/./urandom",
				"-Dfile.encoding=UTF-8",
				"-Duser.timezone=Asia/Seoul",
				"-XX:+UnlockExperimentalVMOptions",
				"-XX:+UseContainerSupport",
				"-XX:+UseG1GC",
				"-XX:InitialHeapSize=1g",
				"-XX:MaxHeapSize=1g",
				"-XX:+DisableExplicitGC", // System.gc() 방어
				"-server",
			)
		ports = listOf(serverPort)
	}
}


plugins {
	java
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("com.google.cloud.tools.jib") version "3.4.2"
}

group = "kr.mafoo"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")
	implementation("org.springframework:spring-jdbc")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.5.0")
	implementation("com.mysql:mysql-connector-j:8.4.0")
	implementation("io.asyncer:r2dbc-mysql:1.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")
	implementation("com.github.f4b6a3:ulid-creator:5.2.3")
	implementation("io.awspring.cloud:spring-cloud-starter-aws:2.4.4")
	implementation("io.micrometer:micrometer-tracing-bridge-otel:1.3.2")
	implementation("io.opentelemetry:opentelemetry-exporter-zipkin:1.40.0")
	implementation("io.micrometer:micrometer-registry-prometheus:1.13.2")
	implementation("com.slack.api:slack-api-client:1.40.3")
	implementation("net.bramp.ffmpeg:ffmpeg:0.8.0")
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

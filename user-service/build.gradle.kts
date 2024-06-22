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
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")
	implementation("org.springframework:spring-jdbc")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.5.0")
	runtimeOnly("org.mariadb:r2dbc-mariadb:1.1.3")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")
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

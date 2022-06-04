import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.6.21"
}

subprojects {
	apply(plugin = "kotlin")

	group = "projects.sahoo"
	version = "0.0.1-SNAPSHOT"
	java.sourceCompatibility = JavaVersion.VERSION_11

	repositories {
		mavenLocal()
		mavenCentral()
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

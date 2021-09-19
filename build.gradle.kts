import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.5.30"
val springBootVersion = "2.5.4"
val queryDslVersion = "5.0.0"

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("java-library")
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    kotlin("plugin.jpa") version "1.5.30"
    kotlin("kapt") version "1.5.30"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

group = "io.alpere.crudfop"
version = "2.0.3"
description = "crudfop"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")
    kapt("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${queryDslVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${queryDslVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.bootJar {enabled = false}
tasks.jar {enabled = true}

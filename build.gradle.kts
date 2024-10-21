plugins {
    id("java")
}

group = "ru.qngdjas"
version = "1.0-SNAPSHOT"

extra.apply {
    set("postgresqlVersion", "42.7.4")
    set("liquibaseVersion", "4.29.2")
    set("junitVersion", "5.10.0")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:${rootProject.extra["postgresqlVersion"]}")
    implementation("org.liquibase:liquibase-core:${rootProject.extra["liquibaseVersion"]}")
    testImplementation(platform("org.junit:junit-bom:${rootProject.extra["junitVersion"]}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runMigrations") {
    mainClass.set("ru.qngdjas.habitstracker.infrastructure.external.postgres.MigrationManager")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runTestMigrations") {
    mainClass.set("ru.qngdjas.habitstracker.infrastructure.external.postgres.MigrationManager")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("TEST")
}
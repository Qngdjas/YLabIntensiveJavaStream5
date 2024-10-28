plugins {
    id("java")
    war
}

group = "ru.qngdjas"
version = "1.0-SNAPSHOT"

extra.apply {
    set("jakartaVersion", "6.0.0")
    set("jacksonVersion", "2.18.0")
    set("mapstructVersion", "1.6.2")
    set("assertJVersion", "1.15.0")
    set("postgresqlVersion", "42.7.4")
    set("liquibaseVersion", "4.29.2")
    set("junitVersion", "5.10.0")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:${rootProject.extra["jakartaVersion"]}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${rootProject.extra["jacksonVersion"]}")
    implementation("org.mapstruct:mapstruct:${rootProject.extra["mapstructVersion"]}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${rootProject.extra["mapstructVersion"]}")
    implementation("org.codehaus.mojo:aspectj-maven-plugin:${rootProject.extra["assertJVersion"]}")
    implementation("org.postgresql:postgresql:${rootProject.extra["postgresqlVersion"]}")
    implementation("org.liquibase:liquibase-core:${rootProject.extra["liquibaseVersion"]}")
    testImplementation(platform("org.junit:junit-bom:${rootProject.extra["junitVersion"]}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:${rootProject.extra["mapstructVersion"]}")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runMigrations") {
    group = "migration"
    description = "Выполнение миграции БД"
    mainClass.set("ru.qngdjas.habitstracker.infrastructure.external.postgres.MigrationManager")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runTestMigrations") {
    group = "migration"
    description = "Выполнение миграции тестовой БД"
    mainClass.set("ru.qngdjas.habitstracker.infrastructure.external.postgres.MigrationManager")
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.register<Copy>("deployToTomcat") {
    group = "deploy"
    dependsOn(tasks.war)
    from(tasks.war.get().archiveFile)
    into("D:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps")
}
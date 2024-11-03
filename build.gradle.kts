plugins {
    id("java")
    id("war")
    id("io.freefair.aspectj.post-compile-weaving") version "8.10.2"
}

group = "ru.qngdjas"
version = "1.0-SNAPSHOT"

val mockitoAgent = configurations.create("mockitoAgent")


extra.apply {
    set("jakartaVersion", "6.0.0")
    set("springVersion", "6.1.14")
    set("lombokVersion", "1.18.34")
    set("jacksonVersion", "2.18.0")
    set("mapstructVersion", "1.6.2")
    set("aspectJVersion", "1.9.22.1")
    set("postgresqlVersion", "42.7.4")
    set("liquibaseVersion", "4.29.2")
    set("junitVersion", "5.10.0")
    set("mockitoVersion", "5.14.2")
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.servlet:jakarta.servlet-api:${rootProject.extra["jakartaVersion"]}")
    implementation("org.springframework:spring-context:${rootProject.extra["springVersion"]}")
    implementation("org.springframework:spring-web:${rootProject.extra["springVersion"]}")
    implementation("org.springframework:spring-webmvc:${rootProject.extra["springVersion"]}")

    compileOnly("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")
    annotationProcessor("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")

    implementation("com.fasterxml.jackson.core:jackson-databind:${rootProject.extra["jacksonVersion"]}")

    implementation("org.mapstruct:mapstruct:${rootProject.extra["mapstructVersion"]}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${rootProject.extra["mapstructVersion"]}")

    implementation("org.aspectj:aspectjrt:${rootProject.extra["aspectJVersion"]}")

    implementation("org.postgresql:postgresql:${rootProject.extra["postgresqlVersion"]}")
    implementation("org.liquibase:liquibase-core:${rootProject.extra["liquibaseVersion"]}")

    testImplementation(platform("org.junit:junit-bom:${rootProject.extra["junitVersion"]}"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.mockito:mockito-core:${rootProject.extra["mockitoVersion"]}")
    mockitoAgent("org.mockito:mockito-core:${rootProject.extra["mockitoVersion"]}") {
        isTransitive = false
    }

    testImplementation("org.springframework:spring-test:${rootProject.extra["springVersion"]}")

    testCompileOnly("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")
    testAnnotationProcessor("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")

    testAnnotationProcessor("org.mapstruct:mapstruct-processor:${rootProject.extra["mapstructVersion"]}")
}

tasks {
    test {
        useJUnitPlatform()
        jvmArgs("-javaagent:${mockitoAgent.asPath}")
    }
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
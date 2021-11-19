plugins {
    java
    `java-library`
    jacoco
    `maven-publish`
}

group = "almazko"
version = "0.6.4-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

java {
    modularity.inferModulePath.set(true)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_9
    targetCompatibility = JavaVersion.VERSION_1_9
}

tasks.named<JavaCompile>("compileTestJava") {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs = listOf("--enable-preview")
}

sourceSets.create("jmh") {
    java.setSrcDirs(listOf("src/jmh/java"))
}

tasks.named<JavaCompile>("compileJmhJava") {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks {
    register("jmh", type = JavaExec::class) {
        dependsOn("jmhClasses")
        group = "benchmark"
        main = "org.openjdk.jmh.Main"
        classpath = sourceSets["jmh"].runtimeClasspath
        args("-wi", "1", "-wf", "1", "-i", "1", "-f", "1")
        jvmArgs("--enable-preview")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.isEnabled = false
        xml.destination = file("${buildDir}/reports/jacoco/report.xml")
    }
}


dependencies {
    "jmhImplementation"(project)
    "jmhImplementation"("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    "jmhImplementation"("com.dslplatform:dsl-json-java8:1.9.5")
    "jmhImplementation"("com.google.code.gson:gson:2.8.6")
    "jmhImplementation"("com.alibaba:fastjson:1.2.68")
    "jmhImplementation"("org.openjdk.jmh:jmh-core:1.23")
    "jmhAnnotationProcessor"("org.openjdk.jmh:jmh-generator-annprocess:1.23")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}


val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/almazko/microjson")
            credentials {
                username = project.findProperty("pub.user")?.toString()
                password = project.findProperty("pub.token")?.toString()
            }
        }
    }
    publications {
        create<MavenPublication>("microjson") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            pom.withXml {
                asNode().apply {
                    appendNode("name", artifactId)
                    appendNode("url", "https://github.com/AlmazKo/microjson")
                }
            }
        }
    }
}

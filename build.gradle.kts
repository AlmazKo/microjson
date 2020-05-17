plugins {
    java
    jacoco
}

group = "micro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    modularity.inferModulePath.set(true)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs = listOf("--enable-preview")
}

sourceSets.create("jmh") {
    java.setSrcDirs(listOf("src/jmh/java"))
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
    compileOnly("org.jetbrains:annotations:19.0.0")

    "jmhImplementation"(project)
    "jmhImplementation"("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    "jmhImplementation"("com.google.code.gson:gson:2.8.6")
    "jmhImplementation"("org.openjdk.jmh:jmh-core:1.23")
    "jmhAnnotationProcessor"("org.openjdk.jmh:jmh-generator-annprocess:1.23")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}





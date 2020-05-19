import java.util.*

plugins {
    java
    jacoco
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

group = "almazko"
version = "0.2"

repositories {
    mavenCentral()
    jcenter()
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
    "jmhImplementation"(project)
    "jmhImplementation"("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    "jmhImplementation"("com.dslplatform:dsl-json-java8:1.9.5")
    "jmhImplementation"("com.google.code.gson:gson:2.8.6")
    "jmhImplementation"("org.openjdk.jmh:jmh-core:1.23")
    "jmhAnnotationProcessor"("org.openjdk.jmh:jmh-generator-annprocess:1.23")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}


val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}


val artifactName = project.name
val artifactGroup = project.group.toString()
val artifactVersion = project.version.toString()
val baseUrl = "https://github.com/AlmazKo/microjson"
val pomUrl = baseUrl
val pomScmUrl = baseUrl
val pomIssueUrl = "$baseUrl/issues"
val pomDesc = baseUrl
val githubRepo = "AlmazKo/microjson"
val githubReadme = "README.md"
val pomLicenseName = "MIT"
val pomLicenseUrl = "https://opensource.org/licenses/mit-license.php"
val pomLicenseDist = "repo"
val pomDeveloperId = "AlmazKo"
val pomDeveloperName = "Alex Suslov"


publishing {
    publications {
        create<MavenPublication>("microjson") {
            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion
            from(components["java"])
            artifact(sourcesJar)
            pom.withXml {
                asNode().apply {
                    appendNode("description", pomDesc)
                    appendNode("name", rootProject.name)
                    appendNode("url", pomUrl)
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", pomLicenseName)
                        appendNode("url", pomLicenseUrl)
                        appendNode("distribution", pomLicenseDist)
                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", pomDeveloperId)
                        appendNode("name", pomDeveloperName)
                    }
                    appendNode("scm").apply {
                        appendNode("url", pomScmUrl)
                    }
                }
            }
        }
    }
}


bintray {
    user = project.findProperty("bintrayUser").toString()
    key = project.findProperty("bintrayKey").toString()
    publish = true
    setPublications("microjson")
    pkg.apply {
        repo = "microjson"
        name = artifactName
        userOrg = "almazko"
        githubRepo = githubRepo
        vcsUrl = pomScmUrl
        description = "JSON tiny library written in modern Java"
        setLabels("json-parser")
        setLicenses("MIT")
        desc = description
        websiteUrl = pomUrl
        issueTrackerUrl = pomIssueUrl
        githubReleaseNotesFile = githubReadme

        version.apply {
            name = artifactVersion
            desc = pomDesc
            released = Date().toString()
            vcsTag = artifactVersion
        }
    }
}

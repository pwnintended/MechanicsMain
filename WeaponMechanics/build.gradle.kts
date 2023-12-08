plugins {
    id("me.deecaad.java-conventions")
}

repositories {
    mavenCentral()
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    api(Dependencies.LATEST_SPIGOT_API)
    implementation(project(":MechanicsCore"))
    implementation(Dependencies.BSTATS)
    implementation("me.cjcrafter:mechanicsautodownload:1.3.1")

    adventureChatAPI()

    compileOnly(Dependencies.PROTOCOL_LIB)
    compileOnly(Dependencies.PLACEHOLDER_API)
    compileOnly(Dependencies.MYTHIC_MOBS)
    compileOnly(Dependencies.VIVECRAFT)
    compileOnly(files(file("../lib/crackshot/CrackShotPlus.jar")))
    compileOnly(files(file("../lib/crackshot/CrackShot.jar")))
}

tasks.test {
    useJUnitPlatform()
}

description = "WeaponMechanics"

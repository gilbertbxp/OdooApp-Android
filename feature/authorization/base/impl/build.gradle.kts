plugins {
    conventions.`module-compose-impl`
}

dependencies {

    Dependencies.RxJava.ALL_DEPS.forEach { implementation(it) }

    // Common
    // Components
    implementation(project(":common:uiKitComponents"))
    // Network - authorization
    api(project(":common:network:authorization:api"))

    // UiKitComponents
    implementation(project(":common:uiKitComponents"))

    // Core
    // UiKitTheme
    implementation(project(":core:uiKitTheme"))

    // Feature
    // Authorization API
    implementation(project(":feature:authorization:base:api"))
    implementation(project(mapOf("path" to ":core:platform")))
}
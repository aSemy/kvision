pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
rootProject.name = "kvision"

include(
    "kvision-modules:kvision-common-annotations",
    "kvision-modules:kvision-common-types",
    "kvision-modules:kvision-common-remote",
    "kvision-modules:kvision-bootstrap",
    "kvision-modules:kvision-bootstrap-css",
    "kvision-modules:kvision-bootstrap-datetime",
    "kvision-modules:kvision-bootstrap-dialog",
    "kvision-modules:kvision-bootstrap-icons",
    "kvision-modules:kvision-bootstrap-select",
    "kvision-modules:kvision-bootstrap-select-remote",
    "kvision-modules:kvision-bootstrap-spinner",
    "kvision-modules:kvision-bootstrap-typeahead",
    "kvision-modules:kvision-bootstrap-typeahead-remote",
    "kvision-modules:kvision-bootstrap-upload",
    "kvision-modules:kvision-onsenui",
    "kvision-modules:kvision-chart",
    "kvision-modules:kvision-chart2",
    "kvision-modules:kvision-cordova",
    "kvision-modules:kvision-datacontainer",
    "kvision-modules:kvision-electron",
    "kvision-modules:kvision-fontawesome",
    "kvision-modules:kvision-handlebars",
    "kvision-modules:kvision-i18n",
    "kvision-modules:kvision-imask",
    "kvision-modules:kvision-jquery",
    "kvision-modules:kvision-maps",
    "kvision-modules:kvision-moment",
    "kvision-modules:kvision-pace",
    "kvision-modules:kvision-print",
    "kvision-modules:kvision-react",
    "kvision-modules:kvision-redux",
    "kvision-modules:kvision-redux-kotlin",
    "kvision-modules:kvision-rest",
    "kvision-modules:kvision-richtext",
    "kvision-modules:kvision-routing-navigo",
    "kvision-modules:kvision-routing-navigo-ng",
    "kvision-modules:kvision-simple-select-remote",
    "kvision-modules:kvision-state",
    "kvision-modules:kvision-state-flow",
    "kvision-modules:kvision-tabulator",
    "kvision-modules:kvision-tabulator-remote",
    "kvision-modules:kvision-tabulator4",
    "kvision-modules:kvision-tabulator4-remote",
    "kvision-modules:kvision-toast",
    "kvision-modules:kvision-server-javalin",
    "kvision-modules:kvision-server-jooby",
    "kvision-modules:kvision-server-ktor",
    "kvision-modules:kvision-server-spring-boot",
    "kvision-modules:kvision-server-vertx",
    "kvision-modules:kvision-server-micronaut",
    "kvision-modules:kvision-testutils",
    "kvision-tools:kvision-compiler-plugin",
    "kvision-tools:kvision-gradle-plugin",
    "kvision-assets",
)


project(":kvision-tools:kvision-gradle-plugin").projectDir = file("./kvision-tools/io.kvision.gradle.plugin")

plugins {
    id ("java-library")
}

dependencies {
    implementation(libs.jakarta.servlet)
    implementation(libs.jakarta.websocket.api)
    implementation(libs.jakarta.websocket.client.api)
    implementation(libs.jetty.ee10.webapp){
        exclude("jetty-jakarta-servlet-api", "org.eclipse.jetty.toolchain")
    }
    implementation(libs.jetty.alpn)
    implementation(libs.jetty.http)
    implementation(libs.jetty.http2.server)
    implementation(libs.jetty.http3.server)
    implementation(libs.jetty.ee10.servlets)
    implementation(libs.jetty.ee10.websocket.jetty.server){
        exclude("jetty-jakarta-servlet-api", "org.eclipse.jetty.toolchain")
        exclude("org.eclipse.jetty", "jetty-jndi")
    }
    implementation(libs.jetty.ee10.websocket.jakarta.server){
        exclude("jetty-jakarta-servlet-api", "org.eclipse.jetty.toolchain")
        exclude("jetty-jakarta-websocket-api", "org.eclipse.jetty.toolchain")
    }
    implementation(libs.tomcat.embed.el)
    implementation(libs.spring.boot)
    implementation(libs.spring.web)
    implementation(libs.spring.autoconfigure)
    implementation(libs.spring.boot.configuration)
}

package org.meteor.boot.jetty.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "jetty")
public class JettyExProperties {
    private List<Protocol> protocol;

    public List<Protocol> getProtocol() {
        return this.protocol;
    }

    public void setProtocol(List<Protocol> protocol) {
        this.protocol = protocol;
    }

    public enum Protocol {
        HTTP1_1("http1.1"),
        HTTP2("h2"),
        HTTP3("h3");
        private final String value;
        Protocol(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}

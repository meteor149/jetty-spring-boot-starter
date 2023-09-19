package org.meteor.boot.jetty.autoconfigure;

import jakarta.servlet.ServletRequest;
import org.meteor.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@AutoConfiguration(after = SslAutoConfiguration.class, before = ServletWebServerFactoryAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(JettyExProperties.class)
public class JettyExAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    static class EmbeddedJetty {
        @Bean
        JettyServletWebServerFactory jettyServletWebServerFactory(
                ObjectProvider<JettyServerCustomizer> serverCustomizers, JettyExProperties jettyExProperties) {
            JettyServletWebServerFactory factory = new JettyServletWebServerFactory(jettyExProperties);
            factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().toList());
            return factory;
        }
    }
}

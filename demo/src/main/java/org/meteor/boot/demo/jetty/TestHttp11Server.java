package org.meteor.boot.demo.jetty;

import org.eclipse.jetty.http2.server.RawHTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class TestHttp11Server implements HttpTester {
    public void testServer () {
        HttpConnectionFactory connectionFactory = new HttpConnectionFactory();

    }

    @Override
    public Connector getConnector(Server server) throws
            UnsupportedEncodingException, URISyntaxException {
        SslContextFactory.Server sslContextFactory = HttpUtils.getSslContextFactory();
        HttpConnectionFactory connectionFactory = new HttpConnectionFactory();
        return new ServerConnector(server, sslContextFactory,
                connectionFactory);
    }
}

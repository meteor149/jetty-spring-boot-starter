package org.meteor.boot.demo.jetty;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public interface HttpTester {
    Connector getConnector(Server server) throws UnsupportedEncodingException, URISyntaxException;
}

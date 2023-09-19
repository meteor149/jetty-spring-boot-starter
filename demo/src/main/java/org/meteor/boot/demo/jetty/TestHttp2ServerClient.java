package org.meteor.boot.demo.jetty;

import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.api.server.ServerSessionListener;
import org.eclipse.jetty.http2.frames.*;
import org.eclipse.jetty.http2.server.RawHTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TestHttp2ServerClient {
    private final Logger logger = LoggerFactory.getLogger("TestHttp2ServerClient");

    public void testServer() throws Exception {
        ServerSessionListener listener = new ServerSessionListener() {
            @Override
            public Map<Integer, Integer> onPreface(Session session) {
                logger.info("Received onPreface %s", session.toString());
                return Map.ofEntries();
            }
            @Override
            public Stream.Listener onNewStream(Stream stream, HeadersFrame frame) {
                MetaData.Request request = (MetaData.Request)frame.getMetaData();
                logger.info("Received request %s", request.getMethod());
                return new Stream.Listener(){
                    @Override
                    public void onDataAvailable(Stream stream) {
                        Stream.Data data = stream.readData();
                        // Get the content buffer.
                        ByteBuffer buffer = data.frame().getByteBuffer();
                        logger.info("Consuming buffer %s", buffer);
                    }
                };
            }
        };
        Server server = HttpUtils.getServer();

        SslContextFactory.Server sslContextFactory = HttpUtils.getSslContextFactory();
        ConnectionFactory connectionFactory = new RawHTTP2ServerConnectionFactory(listener);
        ServerConnector connector = new ServerConnector(server, sslContextFactory, connectionFactory);
        connector.setPort(8080);

        server.addConnector(connector);
        server.start();
    }
}

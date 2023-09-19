package org.meteor.boot.demo.jetty;

import org.eclipse.jetty.http.*;
import org.eclipse.jetty.http2.server.RawHTTP2ServerConnectionFactory;
import org.eclipse.jetty.http3.api.Session;
import org.eclipse.jetty.http3.api.Stream;
import org.eclipse.jetty.http3.frames.DataFrame;
import org.eclipse.jetty.http3.frames.HeadersFrame;
import org.eclipse.jetty.http3.frames.SettingsFrame;
import org.eclipse.jetty.http3.qpack.internal.metadata.Http3Fields;
import org.eclipse.jetty.http3.server.HTTP3ServerConnector;
import org.eclipse.jetty.http3.server.RawHTTP3ServerConnectionFactory;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;

public class TestHttp3ServerClient {
    private final Logger logger = LoggerFactory.getLogger("TestHttp3ServerClient");

    public static void main (String[] args) throws Exception {
        new TestHttp3ServerClient().testServer();
    }

    public void testServer () throws Exception {
        Map.Entry<Long, Long> maxTableCapacity = new AbstractMap.SimpleEntry<>(org.eclipse.jetty.http3.frames.SettingsFrame.MAX_TABLE_CAPACITY, 1024L);
        Map.Entry<Long, Long> maxHeaderSize = new AbstractMap.SimpleEntry<>(org.eclipse.jetty.http3.frames.SettingsFrame.MAX_FIELD_SECTION_SIZE, 2048L);
        Map.Entry<Long, Long> maxBlockedStreams = new AbstractMap.SimpleEntry<>(org.eclipse.jetty.http3.frames.SettingsFrame.MAX_BLOCKED_STREAMS, 16L);

        Session.Server.Listener listener = new Session.Server.Listener() {
            @Override
            public Map<Long, Long> onPreface(Session session) {
                return Map.ofEntries(maxTableCapacity, maxHeaderSize, maxBlockedStreams);
            }

            @Override
            public void onSettings(Session session, SettingsFrame frame) {
                logger.info("onSettings", frame.toString());
            }

            @Override
            public Stream.Server.Listener onRequest(Stream.Server stream, HeadersFrame frame) {
                logger.info("onRequest", frame.toString());
                MetaData.Request request = (MetaData.Request)frame.getMetaData();
                if (frame.isLast()) {
                    respond(stream, request);
                    return null;
                } else {
                    return new Stream.Server.Listener() {
                        @Override
                        public void onDataAvailable(Stream.Server stream) {
                            Stream.Data data = stream.readData();
                            if (data == null) {
                                stream.demand();
                            } else {
                                logger.info("onDataAvailabled", data.getByteBuffer());
                                data.release();
                                if (!data.isLast()) {
                                    stream.demand();
                                }
                            }
                        }
                    };
                }
            }
        };

        Server server = HttpUtils.getServer();

        RawHTTP3ServerConnectionFactory connectionFactory = new RawHTTP3ServerConnectionFactory(listener);
        SslContextFactory.Server sslContextFactory = HttpUtils.getSslContextFactory();
        HTTP3ServerConnector connector = new HTTP3ServerConnector(server, sslContextFactory, connectionFactory);
        Path pemWorkDir = new File(System.getProperty("user.dir")).toPath().resolve("demo/build/tmp/pem");
        pemWorkDir.toFile().mkdirs();
        connector.getQuicConfiguration().setPemWorkDirectory(pemWorkDir);
        connector.setPort(8080);
        
        server.addConnector(connector);
        server.start();
    }

    private void respond(Stream.Server stream, MetaData.Request request) {
        HttpFields.Mutable headers = HttpFields.build();
        headers.put("Alt-Svc", "h3=\":8080\"");
        MetaData.Response response = new MetaData.Response(HttpStatus.OK_200, null, HttpVersion.HTTP_3,
                headers);

        if (HttpMethod.GET.is(request.getMethod())) {
            byte[] data = "Hello HTTP3!".getBytes(StandardCharsets.UTF_8);
            ByteBuffer resourceBytes = ByteBuffer.wrap(data);
            stream.respond(new HeadersFrame(response, false))
                    .thenCompose(s -> s.data(new DataFrame(resourceBytes, true)));
        } else {
            stream.respond(new HeadersFrame(response, true));
        }
    }
}

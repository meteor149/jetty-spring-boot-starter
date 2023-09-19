package org.meteor.boot.demo.jetty;

import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

public class HttpUtils {
    public static <T extends AbstractNetworkConnector> Server getServer() {
        QueuedThreadPool serverThreads = new QueuedThreadPool();
        serverThreads.setName("server");
        return new Server(serverThreads);
    }

    public static SslContextFactory.Server getSslContextFactory() throws UnsupportedEncodingException, URISyntaxException {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        URL keyStore = HttpUtils.class.getClassLoader().getResource("keystore.p12");
        assert !Objects.isNull(keyStore);
        sslContextFactory.setKeyStorePath(Paths.get(keyStore.toURI()).toString());
        sslContextFactory.setKeyStorePassword("storepwd");
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
        sslContextFactory.setUseCipherSuitesOrder(true);
        return sslContextFactory;
    }
}

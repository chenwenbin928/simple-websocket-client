/**
 * Copyright Matthias Weßendorf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wessendorf.websocket.undertow;

import io.undertow.Undertow;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedBinaryMessage;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import net.wessendorf.websocket.AbstractSimpleClientTest;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.logging.Logger;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.websocket;

public class UndertowContainerTest extends AbstractSimpleClientTest {

    private static final Logger LOGGER = Logger.getLogger(UndertowContainerTest.class.getName());

    private Undertow server;

    @Before
    public void bootUndertow() {
        server = Undertow.builder()
                .addHttpListener(9999, "localhost")
                .setHandler(path()
                        .addPrefixPath("/echo", websocket(new WebSocketConnectionCallback() {

                            @Override
                            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                                channel.getReceiveSetter().set(new AbstractReceiveListener() {

                                    @Override
                                    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                                        LOGGER.info("Received Text Message");
                                        WebSockets.sendText(message.getData(), channel, null);
                                    }

                                    @Override
                                    protected void onFullBinaryMessage(WebSocketChannel channel, BufferedBinaryMessage message) throws IOException {
                                        LOGGER.info("Received Binary Message");
                                        WebSockets.sendBinary(message.getData().getResource(), channel, null);
                                    }
                                });
                                channel.resumeReceives();
                            }
                        }))).build();
        server.start();
    }

    @After
    public void shutdownUndertow() {
        server.stop();
    }
}

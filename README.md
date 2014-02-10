simple-websocket-client
=======================

The JSR 356 describes a standard API for WebSocket Server and clients.

This is a simplified version of a WebSocket client, based on the JSR client APIs, basically hiding some of the standard APIs for an easy and simple usage:


```java
final URI securedEndpointURL = new URI("ws://localhost:9999/echo");
final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);

spc.setWebSocketHandler(new WebSocketHandlerAdapter() {
  @Override
  public void onOpen() {
    spc.sendText("Hello"); // ship it!
  }

  @Override
  public void onMessage(String message) {

    assertThat(message).isEqualTo("Hello");

    // close it now:
    spc.close();
  }
});

// connect
spc.connect();
```

Have fun!
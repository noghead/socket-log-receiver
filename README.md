# socket-log-receiver
Simple Spring Boot Netty server to recieve socket logs.

From whereever you want to send logs from; attach a SocketHandler to the logging framework:

SocketHandler socketHandler = new SocketHandler(host, port);
java.util.logging.Logger.getLogger("").addHandler(splunkSocketHandler);


This server will accept those logs; and you can do whatever you want with that log.

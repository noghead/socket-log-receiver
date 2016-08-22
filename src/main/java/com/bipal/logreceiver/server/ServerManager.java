package com.bipal.logreceiver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ServerManager {
    public static final Integer DEFAULT_PORT = 15000;
    public static final String PORT_NUM_PROPERTY = "socket.port";
    public static final String TAG = ServerManager.class.getSimpleName();

    protected Logger logger = Logger.getLogger(TAG);
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;

    private ServerBootstrap serverBootstrap;

    @Autowired
    public ServerManager(EventLoopGroup bossGroup, EventLoopGroup workerGroup,
                         ServerBootstrap serverBootstrap){
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.serverBootstrap = serverBootstrap;
    }

    @PostConstruct
    public void startServerManager(){
        logger.log(Level.INFO, "Starting " + TAG);
        try {
            attachServerBootstrapDependents();
            startListening();
        } catch(InterruptedException e) {
            logger.log(Level.SEVERE, "Error on " + TAG, e);
        } finally {
            logger.log(Level.INFO, "Shutting down " + TAG);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    protected void startListening() throws InterruptedException {
        serverBootstrap.bind(DEFAULT_PORT).sync().channel().closeFuture().sync();
    }

    protected void attachServerBootstrapDependents() {
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(TAG))
                .childHandler(new ServerInitializer());
    }

    @PreDestroy
    public void disposeServerManager(){
        logger.log(Level.INFO, "Disposing " + TAG);
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}

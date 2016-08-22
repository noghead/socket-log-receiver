package com.bipal.logreceiver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by bipal on 3/24/16.
 */
public class ServerManagerTest {
    private Logger mockLogger;
    private ServerManager serverManager;

    @Mock
    private Environment environment;
    @Mock
    private EventLoopGroup bossGroup;
    @Mock
    private EventLoopGroup workerGroup;
    @Mock
    private ServerBootstrap serverBootstrap;


    @Before
    public void setup(){
        initMocks(this);
        mockLogger = mock(Logger.class);
        serverManager = new ServerManager(bossGroup, workerGroup, serverBootstrap);
        serverManager.logger = mockLogger;
    }

    @Test
    public void testDisposeServerManager(){
        serverManager.disposeServerManager();
        verify(workerGroup, times(1)).shutdownGracefully();
        verify(bossGroup, times(1)).shutdownGracefully();
    }

    @Test
    public void testStartServerManagerStartsServer(){
        serverManager = new ServerManager(bossGroup, workerGroup, serverBootstrap){
            @Override
            protected void startListening() throws InterruptedException {
                //do nothing
            }

            @Override
            protected void attachServerBootstrapDependents() {
                //do nothing
            }
        };
        serverManager.logger = mockLogger;

        serverManager.startServerManager();
        String expectedShutDownLog = "Shutting down " + ServerManager.TAG;
        verify(mockLogger, times(1)).log(eq(Level.INFO), eq(expectedShutDownLog));
        verify(workerGroup, times(1)).shutdownGracefully();
        verify(bossGroup, times(1)).shutdownGracefully();
    }

    @Test
    public void testStartServerCatchesInterruptedException(){
        serverManager = new ServerManager(bossGroup, workerGroup, serverBootstrap){
            @Override
            protected void startListening() throws InterruptedException {
                throw new InterruptedException("Test Exception");
            }

            @Override
            protected void attachServerBootstrapDependents() {
               //do nothing
            }
        };
        serverManager.logger = mockLogger;

        serverManager.startServerManager();
        verify(workerGroup, times(1)).shutdownGracefully();
        verify(bossGroup, times(1)).shutdownGracefully();
    }

    @Test
    public void testStartListening() throws InterruptedException {
        try {
            serverManager.startListening();
        }catch(NullPointerException npe){
            //Expected when using mocked objects.
            //DO NOT use non-mocks, netty will try to create server and listen
        }
    }

    @Test
    public void testAttachServerBootstrapDependents(){
        try {
            serverManager.attachServerBootstrapDependents();
        }catch(NullPointerException npe){
            //Expected when using mocked objects.
            //DO NOT use non-mocks, netty will try to create server and listen
        }
    }


}

package com.bipal.logreceiver.server;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by bipal on 3/24/16.
 */
public class ServerInitializerTest {

    private ServerInitializer serverInitializer;

    @Mock
    private Logger mockLogger;

    @Before
    public void setup(){
        initMocks(this);
        serverInitializer = new ServerInitializer();
        serverInitializer.logger = mockLogger;
    }

    @Test
    public void testInitChannel() throws Exception {
        SocketChannel socketChannel = mock(SocketChannel.class);
        ChannelPipeline pipeline = mock(ChannelPipeline.class);
        when(socketChannel.pipeline()).thenReturn(pipeline);

        serverInitializer.initChannel(socketChannel);
        verify(pipeline, times(1)).addLast(ServerInitializer.DECODER);
        verify(pipeline, times(1)).addLast(ServerInitializer.ENCODER);
    }
}

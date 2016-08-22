package com.bipal.logreceiver.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.unix.DomainSocketAddress;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by bipal on 3/24/16.
 */
public class ServerMessageHandlerTest {

    private Logger mockLogger;
    private ServerMessageHandler messageHandler;

    @Before
    public void setup(){
        initMocks(this);
        mockLogger = mock(Logger.class);
        messageHandler = new ServerMessageHandler();
        messageHandler.logger = mockLogger;
    }

    @Test
    public void testExceptionCaught(){
        Throwable mockThrowable = mock(Throwable.class);
        ChannelHandlerContext mockCtx = mock(ChannelHandlerContext.class);
        Channel mockChannel = mock(Channel.class);

        when(mockCtx.channel()).thenReturn(mockChannel);
        when(mockChannel.remoteAddress()).thenReturn(new DomainSocketAddress("testSocketPath"));

        messageHandler.exceptionCaught(mockCtx, mockThrowable);

        verify(mockLogger, times(1)).log(eq(Level.SEVERE), anyString(), eq(mockThrowable));
        verify(mockCtx, times(1)).close();
    }

    @Test
    public void testChannelReadComplete(){
        ChannelHandlerContext mockCtx = mock(ChannelHandlerContext.class);
        Channel mockChannel = mock(Channel.class);

        when(mockCtx.channel()).thenReturn(mockChannel);
        when(mockChannel.remoteAddress()).thenReturn(new DomainSocketAddress("testSocketPath"));

        messageHandler.channelReadComplete(mockCtx);

        verify(mockLogger, times(1)).log(eq(Level.INFO), anyString());
        verify(mockCtx, times(1)).flush();
    }

    @Test
    public void testChannelRead0() throws Exception {
        ChannelHandlerContext mockCtx = mock(ChannelHandlerContext.class);
        Channel mockChannel = mock(Channel.class);

        when(mockCtx.channel()).thenReturn(mockChannel);
        when(mockChannel.remoteAddress()).thenReturn(new DomainSocketAddress("testSocketPath"));
        String someMessage = "SomeMessage";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        messageHandler.channelRead0(mockCtx, someMessage);

        assertEquals(outContent.toString(), someMessage + "\n");
    }

    @Test
    public void testChannelActive() throws Exception {
        ChannelHandlerContext mockCtx = mock(ChannelHandlerContext.class);
        Channel mockChannel = mock(Channel.class);

        when(mockCtx.channel()).thenReturn(mockChannel);
        when(mockChannel.remoteAddress()).thenReturn(new DomainSocketAddress("testSocketPath"));

        messageHandler.channelActive(mockCtx);

        verify(mockLogger, times(1)).log(eq(Level.INFO), anyString());
    }
}

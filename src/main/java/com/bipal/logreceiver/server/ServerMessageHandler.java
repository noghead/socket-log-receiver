package com.bipal.logreceiver.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bipal on 3/24/16.
 */
public class ServerMessageHandler extends SimpleChannelInboundHandler<String> {
    private static final String TAG = ServerMessageHandler.class.getSimpleName();
    protected Logger logger = Logger.getLogger(TAG);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Received new connection from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //Do whatever with the log message here.
        System.out.println(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        logger.log(Level.INFO, "Read Complete for: " + ctx.channel().remoteAddress());
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.log(Level.SEVERE, "Exception Caught in " + TAG + " on connection from: " + ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
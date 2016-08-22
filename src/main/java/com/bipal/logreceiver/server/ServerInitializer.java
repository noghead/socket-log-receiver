package com.bipal.logreceiver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bipal on 3/24/16.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    protected static final StringDecoder DECODER = new StringDecoder();
    protected static final StringEncoder ENCODER = new StringEncoder();

    protected Logger logger = Logger.getLogger(ServerInitializer.class.getSimpleName());

    public ServerInitializer() {}

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        logger.log(Level.FINE, "Initializing Channel for: " + ch.remoteAddress());

        ChannelPipeline pipeline = ch.pipeline();
        // Add the text line codec combination first, Use 8192 (a typical network packet size) for maxFrameLength suggested by a Nitty example
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);

        //Add the message handler.
        pipeline.addLast(new ServerMessageHandler());
    }
}

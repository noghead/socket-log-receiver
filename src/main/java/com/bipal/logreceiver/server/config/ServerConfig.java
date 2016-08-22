package com.bipal.logreceiver.server.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by bipal on 3/24/16.
 */
@Configuration
public class ServerConfig {
    @Bean
    public EventLoopGroup bossGroup(){
        return new NioEventLoopGroup();
    }

    @Bean
    public EventLoopGroup workerGroup(){
        return new NioEventLoopGroup();
    }

    @Bean
    public ServerBootstrap serverBootstrap(){
        return new ServerBootstrap();
    }
}

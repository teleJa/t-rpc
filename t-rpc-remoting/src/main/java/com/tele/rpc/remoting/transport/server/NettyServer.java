package com.tele.rpc.remoting.transport.server;

import com.tele.rpc.remoting.transport.HostInfo;
import com.tele.rpc.remoting.transport.codec.RequestDecoder;
import com.tele.rpc.remoting.transport.codec.ResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class NettyServer
{
    private final String ip;

    private final int port;

    private final AtomicBoolean started = new AtomicBoolean(false);

    private volatile NioEventLoopGroup worker = null;

    private volatile NioEventLoopGroup boss = null;

    public NettyServer(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public void start()
    {

        if (started.compareAndSet(false, true))
        {

           new Thread(()-> {
               worker = new NioEventLoopGroup();
               boss = new NioEventLoopGroup();
               ServerBootstrap serverBootstrap = new ServerBootstrap();
               serverBootstrap.group(boss, worker)
                   .handler(new LoggingHandler(LogLevel.DEBUG))
                   .channel(NioServerSocketChannel.class)
                   .childHandler(new ChannelInitializer<SocketChannel>()
                   {
                       @Override
                       protected void initChannel(SocketChannel ch)
                           throws Exception
                       {
                           ch.pipeline()
                               .addLast(new ResponseEncoder())
                               .addLast(new RequestDecoder())
                               .addLast(new ServerHandler(new HostInfo(ip, port)))
                               .addLast(new LoggingHandler(LogLevel.DEBUG));
                       }
                   });

               try
               {
                   ChannelFuture channelFuture = serverBootstrap.bind(ip, port).sync();
                   if (channelFuture.isSuccess())
                   {
                       log.info("server start success");
                   }
                   channelFuture.channel().closeFuture().sync();
               }
               catch (InterruptedException e)
               {
                   log.error("server start fail", e);
               }
           }).start();

        }

    }

    public void stop()
    {
        if (started.compareAndSet(true, false))
        {
            if (worker != null)
            {
                worker.shutdownGracefully();
            }
            if (boss != null)
            {
                boss.shutdownGracefully();
            }

        }
    }


}

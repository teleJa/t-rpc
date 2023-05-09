package com.tele.rpc.remoting.transport.client;

import com.tele.rpc.remoting.transport.HostInfo;
import com.tele.rpc.remoting.transport.NettyClientStore;
import com.tele.rpc.remoting.transport.codec.RequestEncoder;
import com.tele.rpc.remoting.transport.codec.ResponseDecoder;
import com.tele.rpc.remoting.transport.request.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class NettyClient
{

    private final HostInfo hostInfo;

    private volatile NioEventLoopGroup worker = null;

    private final AtomicBoolean started = new AtomicBoolean(false);

    private volatile Channel channel;

    private final CountDownLatch latch = new CountDownLatch(1);

    public NettyClient(HostInfo hostInfo)
    {
        this.hostInfo = hostInfo;
    }

    public void start()
    {
        if (started.compareAndSet(false, true))
        {
            Thread workThread = new Thread(() -> {
                worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1,
                    new BasicThreadFactory.Builder().namingPattern("client-worker-%d").daemon(true).build());
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(worker).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch)
                            throws Exception
                        {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                                .addLast(new RequestEncoder())
                                .addLast(new ResponseDecoder())
                                .addLast(new ClientHandler(hostInfo))
                                .addLast(new LoggingHandler(LogLevel.DEBUG));
                        }
                    });
                try
                {
                    ChannelFuture channelFuture = bootstrap.connect(hostInfo.getIp(), hostInfo.getPort()).sync();
                    if (channelFuture.isSuccess())
                    {
                        log.debug("connect to server success");
                        this.channel = channelFuture.channel();
                        latch.countDown();
                    }
                    NettyClientStore.addChannel(hostInfo.getHost(), this);
                    channel.closeFuture().sync();
                }
                catch (InterruptedException e)
                {
                    log.error("client start error:", e);
                }
            },"work-thread");
            workThread.setDaemon(true);
            workThread.start();
        }
    }

    public void sendRequest(Request request)
    {
        try
        {
            // client may not start success
            latch.await(10, TimeUnit.SECONDS);
            this.channel.writeAndFlush(request).sync();
        }
        catch (InterruptedException e)
        {
           // ignore
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
        }
    }


}

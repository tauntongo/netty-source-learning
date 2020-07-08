/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

package childChannelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-07-10 23:57
 */
public class OutboundHandlerB extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutboundHandlerB:write->" + msg);
        ctx.write(msg, promise);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.executor().schedule(() -> {
            //channel().write(...)内部任是调用pipeline.write(...)
            ctx.channel().write("I GOT IT");
            //ctx.pipeline().write("I GOT IT");
            //ctx.write("I GOT IT");
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("OutboundHandlerB exceptionCaught");
        ctx.fireExceptionCaught(cause);
    }
}

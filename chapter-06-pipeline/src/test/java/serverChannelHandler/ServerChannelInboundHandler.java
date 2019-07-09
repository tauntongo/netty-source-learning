/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

package serverChannelHandler;import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p></p>
 *
 * @Author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @Date Created in 2019-05-26 16:39
 */
public class ServerChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext context){
        System.out.println("channelActive event");
        context.fireChannelActive();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered event");
        ctx.fireChannelRegistered();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded event");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead event");
        //super.channelRead(ctx, msg);
        //找到下一个handler并继续执行channelRead
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete event");
        ctx.fireChannelReadComplete();
    }
}

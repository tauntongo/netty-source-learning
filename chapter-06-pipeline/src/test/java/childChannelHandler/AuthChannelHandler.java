/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

package childChannelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.ByteBuffer;

/**
 * <p>SimpleChannelInBoundHandler中的实现的channelRead方法可以为我们释放缓冲区，从而我们不必多写代码去释放</p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-07-09 23:22
 */
public class AuthChannelHandler extends SimpleChannelInboundHandler<ByteBuffer> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer password) throws Exception {

        if(pass(password)){
            ctx.pipeline().remove(this);
        }else{
            ctx.close();
        }
    }

    private boolean pass(ByteBuffer password) {
        return false;
    }
}

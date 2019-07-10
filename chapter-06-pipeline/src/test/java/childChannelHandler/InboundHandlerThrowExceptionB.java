/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

package childChannelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p></p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-07-10 23:44
 */
public class InboundHandlerThrowExceptionB extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        throw new Exception("InBoundChannelHandlerThrowExceptionA进行channelRead时出现异常！！！");
    }

}

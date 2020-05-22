/*
 *
 *  * Copyright (c) 2020 tangduns945@gmail.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package server.serverChannelHandler;

import java.util.concurrent.atomic.AtomicInteger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
/**
 *<p></p>
 *@author <a href="mailto:tangdong@supermonkey.com.cn">tangdong</a>
 *@version
 *@since
 *@date Created in 2020-05-22 3:07 下午
 */
public class ChildChannelOutboundHandler extends ChannelOutboundHandlerAdapter {

    private final AtomicInteger writeCounter = new AtomicInteger();

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("child read event");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("服务端写数据次数计数器 ： " + writeCounter.incrementAndGet());
        super.write(ctx, msg, promise);
    }
}

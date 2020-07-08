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

package top.taunton.netty_source_ok.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

/**
 * <p></p>
 *
 * @author <a href="mailto:tangdong@kftpay.com.cn">Taunton</a>
 * @date Created in 2019-07-31 17:23
 * @since
 */
public class Recycler {

	@Test
	public void testPooledMemoryRecycle(){
		PooledByteBufAllocator pooledByteBufAllocator = PooledByteBufAllocator.DEFAULT;
		ByteBuf byteBuf1 = pooledByteBufAllocator.directBuffer(16);
		System.out.println("byteBuf 1 memory address:"+ byteBuf1.memoryAddress());
		byteBuf1.release();
		ByteBuf byteBuf2 = pooledByteBufAllocator.directBuffer(16);
		System.out.println("byteBuf 2 memory address:"+ byteBuf2.memoryAddress());
	}

}

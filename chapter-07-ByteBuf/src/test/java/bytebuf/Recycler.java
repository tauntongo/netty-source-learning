/*
 *
 *  * Copyright (c) 2019 tangduns945@gmail.com.
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

package bytebuf;

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
		ByteBuf byteBuf = pooledByteBufAllocator.heapBuffer(16);
		byteBuf.release();
	}

}

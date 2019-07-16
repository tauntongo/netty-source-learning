/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

package childChannelHandler;/**
 * <p>业务异常</p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-07-10 23:48
 */
public class BizException extends Exception {

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

}

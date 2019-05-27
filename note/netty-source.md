# netty的应用

- ![netty-applied-app](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\netty-applied-app.png)

# Chapter-02

### Netty的基本组件

- NioEventLoop
  - Channel：netty自定义的Channel，是对nio中的Channel的进一步封装
  - Pipeline
  - ChannelHandler：每一次Channel需要进行数据处理就放出一个ChannelHandler来进行处理，我们可以创建多个ChannelHandler对象添加到Channel里面去，从而介入Channel的数据处理流程中去。我们的业务代码也写在这里面。
  - ByteBuffer



# Chapter-03(服务端启动&服务端启动时干了什么)

### QuickStart-启动一个Netty服务端

```java
public class StartServerDemo {

    @Test
    public void server(){
        EventLoopGroup serverGroup = new NioEventLoopGroup(1);
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(serverGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    // 对服务端Channel NioServerSocketChannel的配置，可通过多次调用设置多个
                    //.option()
                    //.option()
                    // 对服务端Channel NioServerSocketChannel的属性设置，可通过多次调用设置多个
                    //.attr()
                    //.attr()
                    //对accept到的SocketChannel的配置，每次accept到SocketChannel都会按照我们所传的配置项配置一遍
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //对accept到的SocketChannel的属性设置，每次accept到SocketChannel都会按照我们所传的属性设置一遍
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    //对NioServerSocketChannel的处理介入，这里我们传入的是一个继承了ChannelInboundHandlerAdapter的自定义handler对象
                    .handler(new ServerHandler())
                    //对accept到的SocketChannel处理介入,我们如果要写业务代码一般也就是写在handler里面了
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以在这这这里加上众多的handler介入对accept到的Channel的处理
                            //ch.pipeline().addLast()
                            //ch.pipeline().addLast()
                            //ch.pipeline().addAfter()
                        }
                    });

            //启动Netty服务端
            ChannelFuture channelFuture = b.bind(8848).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            serverGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            //childGroup.shutdownGracefully(1000,3000, TimeUnit.MILLISECONDS);
        }
    }
}
```

### 服务端启动流程

- 服务端启动核心路径

```mermaid
sequenceDiagram
new Channel()->>init():then
init()->>doBind():then
doBind()->>register():then
```


##### 服务端Channel的创建

- 从外部用户代码进入到具体创建这一步的流程

  - ```mermaid
    sequenceDiagram
    bind()[用户代码入口]->>initAndRegister()[初始化并注册]:then
    initAndRegister()[初始化并注册]->>newChannel()[通过反射创建服务端channel]:then
    ```

- 具体创建的流程

  - newSocket()：通过jdk来创建底层jdk channel
    - ![2019-05-26-2.1-newSocket()](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-26-2.1-newSocket().png)
  - AbstractNioChannel()
    - AbstractChannel()：创建id，unsafe，pipeline
    - SelectableChannel.configureBlocking(false)：阻塞模式
    - ![2019-05-26-2.2-AbstractNioChannel()](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-26-2.2-AbstractNioChannel().png)
  - new NioServerSocketChannelConfig()：tcp参数配置类。通过ServerBootstrap.option()保存的参数最终会被设置到此配置对象中去



##### 服务端Channel的初始化

- 从外部用户代码进入到具体初始化这一步的流程

  - ```mermaid
    sequenceDiagram
    bind()[用户代码入口]->>initAndRegister()[初始化并注册]:then
    initAndRegister()[初始化并注册]->>newChannel()[通过反射创建服务端channel]:then
    initAndRegister()[初始化并注册]->>init()[初始化服务端channel]:then
    ```

- 具体初始化Channel流程

  - init()：初始化入口
    - set ChannelOptions，set ChannelAttrs：将用户配置的参数&属性设置到NioServerSocketChannel中去
    - childGroup、childHandler、childOptions、childAttrs：获取到对请求Channel的各种配置，用来在后面第四步传入连接器中
    - config.handler()：配置服务端pipeline
    - add ServerBootstrapAcceptor：添加连接器（每次accept到SocketChannel后使用用户配置的childGroup、childHandler、childOptions、childAttrs作为其配置）
    - 源码：
      - ![2019-05-27-1-ServerBootstrap.init()](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-27-1-ServerBootstrap.init().png)

##### 注册到selector

![2019-05-26-6注册selector](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-26-6注册selector.png)



##### 服务端口的绑定

![2019-05-26-7-端口绑定](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-26-7-端口绑定.png)

# Chapter-04

# Chapter-05

# Chapter-06


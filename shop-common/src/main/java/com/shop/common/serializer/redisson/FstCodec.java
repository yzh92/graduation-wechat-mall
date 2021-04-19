package com.shop.common.serializer.redisson;

import com.shop.common.serializer.FSTSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

/**
 * 被redisson使用
 * Efficient and speedy serialization codec fully
 * 高效和快速的序列化编解码器
 * compatible with JDK Serialization codec.
 *兼容JDK序列化编解码器。
 * https://github.com/RuedigerMoeller/fast-serialization
 *
 * @author yzh
 *
 */
public class FstCodec extends BaseCodec {


    private final FSTConfiguration config;

    public FstCodec() {
        config = new FSTSerializer().getConfig();
    }


    private final Decoder<Object> decoder = new Decoder<Object>() {
        @Override
        public Object decode(ByteBuf buf, State state) throws IOException {
            ByteBufInputStream in = new ByteBufInputStream(buf);
            FSTObjectInput inputStream = config.getObjectInput(in);
            try {
                return inputStream.readObject();
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    };

    private final Encoder encoder = new Encoder() {

        @Override
        public ByteBuf encode(Object in) throws IOException {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            ByteBufOutputStream os = new ByteBufOutputStream(out);
            FSTObjectOutput oos = config.getObjectOutput(os);
            try {
                oos.writeObject(in);
                oos.flush();
                return os.buffer();
            } catch (IOException e) {
                out.release();
                throw e;
            } catch (Exception e) {
                out.release();
                throw new IOException(e);
            }
        }
    };

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

    @Override
    public ClassLoader getClassLoader() {
        if (config.getClassLoader() != null) {
            return config.getClassLoader();
        }

        return super.getClassLoader();
    }

}

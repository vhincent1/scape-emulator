//package net.scapeemulator.game.net.game;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.embedded.EmbeddedByteChannel;
//import net.scapeemulator.game.net.game.GameFrame.Type;
//import net.scapeemulator.util.crypto.ZeroStreamCipher;
//import org.junit.Test;
//
//import static org.junit.Assert.assertArrayEquals;
//
//public final class TestGameFrameEncoder {
//
//    @Test
//    public void testEmpty() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{12});
//        ByteBuf payload = Unpooled.EMPTY_BUFFER;
//        GameFrame frame = new GameFrame(12, Type.FIXED, payload);
//        testFrame(header, payload, frame);
//    }
//
//    @Test
//    public void testFixed() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{7});
//        ByteBuf payload = Unpooled.wrappedBuffer(new byte[]{10, 11, 12});
//        GameFrame frame = new GameFrame(7, Type.FIXED, payload.slice());
//        testFrame(header, payload, frame);
//    }
//
//    @Test
//    public void testVariableByte() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{110, 4});
//        ByteBuf payload = Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4});
//        GameFrame frame = new GameFrame(110, Type.VARIABLE_BYTE, payload.slice());
//        testFrame(header, payload, frame);
//    }
//
//    @Test
//    public void testVariableShort() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{73, 0, 5});
//        ByteBuf payload = Unpooled.wrappedBuffer(new byte[]{5, 6, 7, 8, 9});
//        GameFrame frame = new GameFrame(73, Type.VARIABLE_SHORT, payload.slice());
//        testFrame(header, payload, frame);
//    }
//
//    private void testFrame(ByteBuf header, ByteBuf payload, GameFrame frame) {
//        EmbeddedByteChannel ch = new EmbeddedByteChannel(new GameFrameEncoder(new ZeroStreamCipher()));
//        ch.write(frame);
//
//        assertArrayEquals(ch.readOutbound().copy().array(), Unpooled.wrappedBuffer(header, payload).copy().array());
//    }
//
//}

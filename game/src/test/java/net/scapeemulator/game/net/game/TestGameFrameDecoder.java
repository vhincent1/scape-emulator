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
//import static org.junit.Assert.assertEquals;
//
//public final class TestGameFrameDecoder {
//
//    @Test
//    public void testEmpty() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{(byte) 184});
//        testFrame(header, Unpooled.EMPTY_BUFFER, 184, Type.FIXED);
//    }
//
//    @Test
//    public void testEmptyVariable() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{(byte) 215, 0});
//        ByteBuf payload = Unpooled.EMPTY_BUFFER;
//        testFrame(header, payload, 215, Type.VARIABLE_BYTE);
//    }
//
//    @Test
//    public void testFixed() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{(byte) 157});
//        ByteBuf payload = Unpooled.wrappedBuffer(new byte[]{1, 2, 3});
//        testFrame(header, payload, 157, Type.FIXED);
//    }
//
//    @Test
//    public void testVariable() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{(byte) 215, 5});
//        ByteBuf payload = Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4, 5});
//        testFrame(header, payload, 215, Type.VARIABLE_BYTE);
//    }
//
//    @Test
//    public void testFragmented() {
//        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{(byte) 215, 5});
//        ByteBuf payload = Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4, 5});
//
//        EmbeddedByteChannel ch = new EmbeddedByteChannel(new GameFrameDecoder(new ZeroStreamCipher()));
//        ch.writeInbound(header.slice(0, 1));
//        ch.writeInbound(header.slice(1, 1));
//        ch.writeInbound(payload.slice(0, 1));
//        ch.writeInbound(payload.slice(1, 4));
//
//        GameFrame frame = (GameFrame) ch.readInbound();
//        assertEquals(215, frame.opcode);
//        assertEquals(Type.VARIABLE_BYTE, frame.type);
//        assertArrayEquals(payload.copy().array(), frame.payload.copy().array());
//    }
//
//    private void testFrame(ByteBuf header, ByteBuf payload, int opcode, Type type) {
//        EmbeddedByteChannel ch = new EmbeddedByteChannel(new GameFrameDecoder(new ZeroStreamCipher()));
//        ch.writeInbound(Unpooled.wrappedBuffer(header, payload));
//
//        GameFrame frame = (GameFrame) ch.readInbound();
//        assertEquals(opcode, frame.opcode);
//        assertEquals(type, frame.type);
//        assertArrayEquals(payload.copy().array(), frame.payload.copy().array());
//    }
//
//}

package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.WalkMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class WalkMessageDecoder(opcode: Int) : MessageDecoder<WalkMessage>(opcode) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): WalkMessage {
        val reader = GameFrameReader(frame)

        val anticheat = frame.opcode == 39
        val stepCount = (reader.length - (if (anticheat) 19 else 5)) / 2 + 1
        val running = reader.getUnsigned(DataType.BYTE, DataTransformation.ADD) == 1L
        val x = reader.getUnsigned(DataType.SHORT).toInt()
        val y = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()


        val steps = arrayOfNulls<WalkMessage.Step>(stepCount)
        steps[0] = WalkMessage.Step(x, y)
        for (i in 1..<stepCount) {
            val stepX = x + reader.getSigned(DataType.BYTE, DataTransformation.ADD).toInt()
            val stepY = y + reader.getSigned(DataType.BYTE, DataTransformation.SUBTRACT).toInt()
            steps[i] = WalkMessage.Step(stepX, stepY)
        }

        return WalkMessage(steps, running)
    }
}

//package net.scapeemulator.game.msg.codec;
//
//import net.scapeemulator.game.msg.WalkMessage;
//import net.scapeemulator.game.msg.WalkMessage.Step;
//import net.scapeemulator.game.net.game.DataTransformation;
//import net.scapeemulator.game.net.game.DataType;
//import net.scapeemulator.game.net.game.GameFrame;
//import net.scapeemulator.game.net.game.GameFrameReader;
//
//import java.io.IOException;
//
//public final class WalkMessageDecoder extends MessageDecoder<WalkMessage> {
//
//    public WalkMessageDecoder(int opcode) {
//        super(opcode);
//    }
//
//    @Override
//    public WalkMessage decode(GameFrame frame) throws IOException {
//        GameFrameReader reader = new GameFrameReader(frame);
//
//        boolean anticheat = frame.opcode == 39;
//        int stepCount = (reader.getLength() - (anticheat ? 19 : 5)) / 2 + 1;
//        boolean running = reader.getUnsigned(DataType.BYTE, DataTransformation.ADD) == 1;
//        int x = (int) reader.getUnsigned(DataType.SHORT);
//        int y = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
//
//        Step[] steps = new Step[stepCount];
//        steps[0] = new Step(x, y);
//        for (int i = 1; i < stepCount; i++) {
//        int stepX = x + (int) reader.getSigned(DataType.BYTE, DataTransformation.ADD);
//        int stepY = y + (int) reader.getSigned(DataType.BYTE, DataTransformation.SUBTRACT);
//        steps[i] = new Step(stepX, stepY);
//    }
//
//        return new WalkMessage(steps, running);
//    }
//
//}

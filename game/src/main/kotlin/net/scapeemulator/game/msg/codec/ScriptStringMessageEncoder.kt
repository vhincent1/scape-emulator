package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ScriptStringMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val ScriptStringMessageEncoder = MessageEncoder(ScriptStringMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 123)
    builder.put(DataType.SHORT, DataOrder.LITTLE, message.id)
    builder.put(DataType.SHORT, DataTransformation.ADD, 0)
    builder.putString(message.value)
    return@MessageEncoder builder.toGameFrame()
    /* TODO what is the difference between these two packets? they seem identical
    GameFrameBuilder builder = new GameFrameBuilder(alloc, 48);
    builder.put(DataType.SHORT, 0);
    builder.putString(message.getValue());
    builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
    return builder.toGameFrame();
*/
}

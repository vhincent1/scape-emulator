package net.scapeemulator.game.net.world

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import net.scapeemulator.util.ByteBufUtils

class WorldListEncoder : MessageToByteEncoder<WorldListMessage>() {

    public override fun encode(ctx: ChannelHandlerContext, list: WorldListMessage, out: ByteBuf) {
        val buf = ctx.alloc().buffer()
        buf.writeByte(1)
        buf.writeByte(1)

        val countries = list.countries
        ByteBufUtils.writeSmart(buf, countries.size)

        for (country in countries) {
            ByteBufUtils.writeSmart(buf, country.flag)
            ByteBufUtils.writeWorldListString(buf, country.name)
        }

        val worlds = list.worlds
        var minId = worlds[0].id
        var maxId = worlds[0].id
        for (i in 1..<worlds.size) {
            val world = worlds[i]
            val id = world.id

            if (id > maxId) maxId = id
            if (id < minId) minId = id
        }

        ByteBufUtils.writeSmart(buf, minId)
        ByteBufUtils.writeSmart(buf, maxId)
        ByteBufUtils.writeSmart(buf, worlds.size)

        for (world in worlds) {
            ByteBufUtils.writeSmart(buf, world.id - minId)
            buf.writeByte(world.country)
            buf.writeInt(world.flags)
            ByteBufUtils.writeWorldListString(buf, world.activity)
            ByteBufUtils.writeWorldListString(buf, world.ip)
        }

        buf.writeInt(list.sessionId)

        val players = list.players
        for (i in worlds.indices) {
            val world = worlds[i]
            ByteBufUtils.writeSmart(buf, world.id - minId)
            buf.writeShort(players[i])
        }

        out.writeByte(0) // 0 = ok, 7/9 = world full
        out.writeShort(buf.readableBytes())
        out.writeBytes(buf)
    }
}

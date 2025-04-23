package net.scapeemulator.game.net.game

import io.netty.buffer.ByteBuf
import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import net.scapeemulator.util.crypto.StreamCipher
import java.io.IOException

class GameFrameDecoder(private val cipher: StreamCipher) : ByteToMessageDecoder() {
    private enum class State {
        READ_OPCODE, READ_SIZE, READ_PAYLOAD
    }

    private var state = State.READ_OPCODE
    private var variable = false
    private var opcode = 0
    private var size = 0

    @Throws(Exception::class)
    public override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MessageBuf<Any>) {
        if (state == State.READ_OPCODE) {
            if (!buf.isReadable()) return

            opcode = (buf.readUnsignedByte() - cipher.nextInt()) and 0xFF
            size = SIZES[opcode]

            if (size == -3) throw IOException("Illegal opcode " + opcode + ".")

            variable = size == -1
            state = if (variable) State.READ_SIZE else State.READ_PAYLOAD
        }

        if (state == State.READ_SIZE) {
            if (!buf.isReadable()) return

            size = buf.readUnsignedByte().toInt()
            state = State.READ_PAYLOAD
        }

        if (state == State.READ_PAYLOAD) {
            if (buf.readableBytes() < size) return

            val payload = buf.readBytes(size)
            state = State.READ_OPCODE

            out.add(GameFrame(opcode, if (variable) GameFrame.Type.VARIABLE_BYTE else GameFrame.Type.FIXED, payload))
        }
    }

    companion object {
        private val SIZES = IntArray(256)

        init {
            SIZES[0] = 0
            SIZES[1] = -3
            SIZES[2] = -3
            SIZES[3] = 2 // Attack NPC
            SIZES[4] = -3
            SIZES[5] = -3
            SIZES[6] = -3
            SIZES[7] = -3
            SIZES[8] = -3
            SIZES[9] = -3
            SIZES[10] = 4 // Actionbuttons #2
            SIZES[11] = -3
            SIZES[12] = -3
            SIZES[13] = -3
            SIZES[14] = -3
            SIZES[15] = -3
            SIZES[16] = -3
            SIZES[17] = -3
            SIZES[18] = -3
            SIZES[19] = -3
            SIZES[20] = -3
            SIZES[21] = 4 // Camera
            SIZES[22] = 1 // Focus
            SIZES[23] = 4 // Enter amount
            SIZES[24] = -3
            SIZES[25] = -3
            SIZES[26] = -3
            SIZES[27] = 16 // Item on item
            SIZES[28] = -3
            SIZES[29] = -3
            SIZES[30] = 2 // Fourth click NPC (trade slayermaster).
            SIZES[31] = -3
            SIZES[32] = -3
            SIZES[33] = -3
            SIZES[34] = 8 // Add ignore
            SIZES[35] = -3
            SIZES[36] = -3
            SIZES[37] = -3
            SIZES[38] = -3
            SIZES[39] = -1 // Walk
            SIZES[40] = -3
            SIZES[41] = -3
            SIZES[42] = -3
            SIZES[43] = -3
            SIZES[44] = -1 // Command
            SIZES[45] = -3
            SIZES[46] = -3
            SIZES[47] = -3
            SIZES[48] = -3
            SIZES[49] = -3
            SIZES[50] = -3
            SIZES[51] = -3
            SIZES[52] = -3
            SIZES[53] = 6 // Interface option #9
            SIZES[54] = -3
            SIZES[55] = 8 // Equip item
            SIZES[56] = -3
            SIZES[57] = 8 // Delete friend
            SIZES[58] = -3
            SIZES[59] = -3
            SIZES[60] = -3
            SIZES[61] = -3
            SIZES[62] = -3
            SIZES[63] = -3
            SIZES[64] = 6 // Interface option #8
            SIZES[65] = -3
            SIZES[66] = 6 // Pick up item
            SIZES[67] = -3
            SIZES[68] = 2 // Attack player
            SIZES[69] = -3
            SIZES[70] = -3
            SIZES[71] = 2 // Trade player
            SIZES[72] = -3
            SIZES[73] = -3
            SIZES[74] = -3
            SIZES[75] = 6 // Mouse click
            SIZES[76] = -3
            SIZES[77] = -1 // Walk
            SIZES[78] = 2 // Second click NPC
            SIZES[79] = 12 // Swapping inventory places in shop, bank and duel
            SIZES[80] = -3
            SIZES[81] = 8 // Unequip item
            SIZES[82] = -3
            SIZES[83] = -3
            SIZES[84] = 6 // Object third click
            SIZES[85] = -3
            SIZES[86] = -3
            SIZES[87] = -3
            SIZES[88] = -3
            SIZES[89] = -3
            SIZES[90] = -3
            SIZES[91] = -3
            SIZES[92] = 2 // Inventory item examine.
            SIZES[93] = 0 // Ping
            SIZES[94] = 2 // ?
            SIZES[95] = -3
            SIZES[96] = -3
            SIZES[97] = -3
            SIZES[98] = 4 // Toggle sound setting
            SIZES[99] = 10 // ?
            SIZES[100] = -3
            SIZES[101] = -3
            SIZES[102] = -3
            SIZES[103] = -3
            SIZES[104] = 8 // Join clan chat
            SIZES[105] = -3
            SIZES[106] = 2 // Follow player
            SIZES[107] = -3
            SIZES[108] = -3
            SIZES[109] = -3
            SIZES[110] = 0 // Region loading, size varies
            SIZES[111] = 2 // Grand Exchange item search
            SIZES[112] = -3
            SIZES[113] = -3
            SIZES[114] = -3
            SIZES[115] = 10 // Use item on npc
            SIZES[116] = -3
            SIZES[117] = -3
            SIZES[118] = -3
            SIZES[119] = -3
            SIZES[120] = 8 // Add friend
            SIZES[121] = -3
            SIZES[122] = -3
            SIZES[123] = -3
            SIZES[124] = 6 //Interface option #3
            SIZES[125] = -3
            SIZES[126] = -3
            SIZES[127] = -3
            SIZES[128] = -3
            SIZES[129] = -3
            SIZES[130] = -3
            SIZES[131] = -3
            SIZES[132] = 6 // Actionbuttons #3
            SIZES[133] = -3
            SIZES[134] = 14 // Item on object
            SIZES[135] = 8 // Drop item
            SIZES[136] = -3
            SIZES[137] = 4 // Unknown, nothing major
            SIZES[138] = -3
            SIZES[139] = -3
            SIZES[140] = -3
            SIZES[141] = -3
            SIZES[142] = -3
            SIZES[143] = -3
            SIZES[144] = -3
            SIZES[145] = -3
            SIZES[146] = -3
            SIZES[147] = -3
            SIZES[148] = 2 // Third click NPC
            SIZES[149] = -3
            SIZES[150] = -3
            SIZES[151] = -3
            SIZES[152] = -3
            SIZES[153] = 8 // Inventory click item #2 (check RC pouch)
            SIZES[154] = -3
            SIZES[155] = 6 // Actionbutton
            SIZES[156] = 8 // Inventory click item (food etc)
            SIZES[157] = 3 // Privacy options
            SIZES[158] = -3
            SIZES[159] = -3
            SIZES[160] = -3
            SIZES[161] = 8 // Item right click option #1 (rub/empty)
            SIZES[162] = 8 // Clan chat kick
            SIZES[163] = -3
            SIZES[164] = -3
            SIZES[165] = -3
            SIZES[166] = 6 // Interface option #7
            SIZES[167] = -1
            SIZES[168] = 6 // Interface option #6
            SIZES[169] = -3
            SIZES[170] = -3
            SIZES[171] = -3
            SIZES[172] = -3
            SIZES[173] = -3
            SIZES[174] = -3
            SIZES[175] = -3
            SIZES[176] = -3
            SIZES[177] = 2 // Junk, no real purpose
            SIZES[178] = -3
            SIZES[179] = -3
            SIZES[180] = 2 // Accept trade (chatbox)
            SIZES[181] = -3
            SIZES[182] = -3
            SIZES[183] = -3
            SIZES[184] = 0 // Close interface
            SIZES[185] = -3
            SIZES[186] = -3
            SIZES[187] = -3
            SIZES[188] = 9 // Clan ranks
            SIZES[189] = -3
            SIZES[190] = -3
            SIZES[191] = -3
            SIZES[192] = -3
            SIZES[193] = -3
            SIZES[194] = 6 // Object second click
            SIZES[195] = 8 // Magic on player
            SIZES[196] = 6 // Interface option #2
            SIZES[197] = -3
            SIZES[198] = -3
            SIZES[199] = 6 //Interface option #4
            SIZES[200] = -3
            SIZES[201] = -1 // Send PM
            SIZES[202] = -3
            SIZES[203] = -3
            SIZES[204] = -3
            SIZES[205] = -3
            SIZES[206] = 8 // Operate item
            SIZES[207] = -3
            SIZES[208] = -3
            SIZES[209] = -3
            SIZES[210] = -3
            SIZES[211] = -3
            SIZES[212] = -3
            SIZES[213] = 8 // Delete ignore
            SIZES[214] = -3
            SIZES[215] = -1 // Walk
            SIZES[216] = -3
            SIZES[217] = -3
            SIZES[218] = 2 // Fifth click NPC
            SIZES[219] = -3
            SIZES[220] = -3
            SIZES[221] = -3
            SIZES[222] = -3
            SIZES[223] = -3
            SIZES[224] = -3
            SIZES[225] = -3
            SIZES[226] = -3
            SIZES[227] = -3
            SIZES[228] = -3
            SIZES[229] = -3
            SIZES[230] = -3
            SIZES[231] = 9 // Swap item slot
            SIZES[232] = -3
            SIZES[233] = -3
            SIZES[234] = 6 //Interface option #5
            SIZES[235] = -3
            SIZES[236] = -3
            SIZES[237] = -1 // Public chat
            SIZES[238] = -3
            SIZES[239] = 8 // Magic on NPC
            SIZES[240] = -3
            SIZES[241] = -3
            SIZES[242] = -3
            SIZES[243] = 6 // Screen type (fullscreen, small HD etc)
            SIZES[244] = 8 // Enter text
            SIZES[245] = 0 // Idle logout
            SIZES[246] = -3
            SIZES[247] = 6 // Object 4th option
            SIZES[248] = -3
            SIZES[249] = -3
            SIZES[250] = -3
            SIZES[251] = -3
            SIZES[252] = -3
            SIZES[253] = -3
            SIZES[254] = 6 // First click object
            SIZES[255] = -3
        }
    }
}

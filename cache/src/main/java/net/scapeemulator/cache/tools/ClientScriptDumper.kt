package net.scapeemulator.cache.tools

import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container.Companion.decode
import net.scapeemulator.cache.FileStore.Companion.open
import net.scapeemulator.cache.ReferenceTable
import net.scapeemulator.cache.def.ClientScript
import java.io.IOException

object ClientScriptDumper {
    private val opcodes: MutableMap<Int, String> = HashMap()

    init {
        opcodes[0] = "pushi"
        opcodes[1] = "pushi_cfg"
        opcodes[2] = "popi_cfg"
        opcodes[3] = "pushs"
        opcodes[6] = "goto"
        opcodes[7] = "if_ne"
        opcodes[8] = "if_eq"
        opcodes[9] = "if_lt"
        opcodes[10] = "if_gt"
        opcodes[21] = "return"
        opcodes[25] = "pushi_varbit"
        opcodes[26] = "popi_varbit"
        opcodes[31] = "if_lteq"
        opcodes[32] = "if_gteq"
        opcodes[33] = "loadi"
        opcodes[34] = "storei"
        opcodes[35] = "loads"
        opcodes[36] = "stores"
        opcodes[37] = "concat_str"
        opcodes[38] = "popi"
        opcodes[39] = "pops"
        opcodes[40] = "call"
        opcodes[42] = "loadi_global"
        opcodes[43] = "storei_global"
        opcodes[44] = "dim"
        opcodes[45] = "push_array"
        opcodes[46] = "pop_array"
        opcodes[47] = "loads_global"
        opcodes[48] = "stores_global"
        opcodes[51] = "switch"
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Cache(open("../game/data/cache/")).use { cache ->
            val rt = ReferenceTable.decode(decode(cache.store.read(255, 12)).getData())
            for (id in 0 until rt.capacity()) {
                val entry = rt.getEntry(id) ?: continue

                val script = ClientScript.decode(cache.read(12, id).getData())
                println("===== $id ======")
                for (op in 0 until script.length) {
                    val opcode = script.getOpcode(op)

                    val str = script.getStrOperand(op)
                    val `val` = script.getIntOperand(op)

                    var name = opcodes[opcode]
                    if (name == null) name = "op$opcode"

                    val param = str ?: `val`.toString()
                    println("$op $name $param")
                }
                println()
            }
        }
    }
}

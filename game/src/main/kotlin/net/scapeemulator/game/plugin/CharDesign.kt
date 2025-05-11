package net.scapeemulator.game.plugin

import net.scapeemulator.game.command.handleCommand
import net.scapeemulator.game.model.*
import net.scapeemulator.game.model.Appearance.Companion.FEMALE_APPEARANCE
import net.scapeemulator.game.model.Appearance.Companion.MALE_APPEARANCE
import net.scapeemulator.game.msg.AnimateInterfaceMessage
import net.scapeemulator.game.msg.ConfigMessage
import net.scapeemulator.game.msg.DisplayModelMessage
import kotlin.random.Random

/*
Mouse Settings:-
[170] 1	- 1 Mouse button
[170] 0	- 2 Mouse buttons

Male/Female:-
[1159] 10312139	- Woman
[1158] 19305486 - Woman
[1262] 8		- Woman
[1159] 592392	- Man
[1158] 15541890 - Man
[1262] 1		- Man

Head:-
[1158]:-
( Beard	 + 	Hair )
3577856 -> 3577856
3577888 -> 3577903
3577920 -> 3577935
3577952 -> 3577967
3577984 -> 3577999
3578016 -> 3578031
3578048 -> 3578063
3578080 -> 3578095
3578112 -> 3578127
3578144 -> 3578159
3578176 -> 3578191
3578208 -> 3578223
3578240 -> 3578255
3578272 -> 3578287
3578304 -> 3578319
[1159]:-
68	- Burgundy
67	- Red
58	- Vermillon
66	- Pink
52	- Orange
53	- Yellow
63	- Peach
55	- Brown
48	- Dark Brown
54	- Light Brown
69	- Mint Green
57	- Green
70	- Dark Green
65	- Dark Blue
56	- Terquise
64	- Cyan
59	- Purple
72	- Violet
71	- Indigo
51	- Dark Gray
50	- Military Gray
49	- White
62	- Light Gray
61	- Tawpe
60	- Black
1853281	- Very Pale
18273	- Pale
280417	- Fair
542561	- Tanned
804705	- Native
1066849	- Light Brown
1032993	- Brown
1591137	- Dark Brown
[1260] 0 - Don't Ask me this again (Randomize)(Off)
[1260] 1 - Don't Ask me this again (Randomize)(On)

Body:-
NEED TO DO.

*/
class CharDesign {

    private val attributes: MutableMap<String, Any> = HashMap()

    fun <T> getAttribute(username: String, key: String, default: T): T {
        return (attributes[username + key] ?: default) as T
    }

    fun open(player: Player) {
        println("Open")
        player.sendMessage("Open")
        player.appearance = player.appearance
        player.send(DisplayModelMessage(771, 79))
        player.send(AnimateInterfaceMessage(9806, 771, 79))
        player.interfaceSet.openWindow(771) //{ close event}
    }

    //TODO: fix
    private fun updateLook(player: Player, body: Body, increment: Boolean) {
        val type = "char-design:look" + body.ordinal
        println("TYPE: $type")
        val max = body.getIds(player.appearance.gender).size - 1
        println("Max: $max")
        println("Incr: increment: $increment")
//        var value: Int = player.appearance.getBody(body) + (if (increment) 1 else -1)
        val attr = attributes[player.username + type] ?: 0
        var value: Int = attr as Int + (if (increment) 1 else -1)

        if (value < 0)
            value = max
        else if (value > max)
            value = 0
        println("Index: $value")

        val bodyIds = body.getIds(player.appearance.gender)
        val bodyPart = bodyIds[value]

        println("${body.name}=${bodyPart}")

        attributes.put(player.username + body.name, bodyPart)
        attributes.put(player.username + type, value)

        player.appearance = player.appearance.apply {
            setBody(body, bodyPart)
        }
    }

    fun handleButtons(player: Player, buttonId: Int) {
        when (buttonId) {
            37, 40 -> {
                player.settings.toggleTwoButtonMouse()
            }
            // gender
            49, 52 -> {
                val female = buttonId == 52
                //todo fix
                player.appearance = if (female)
                    FEMALE_APPEARANCE
                else
                    MALE_APPEARANCE
                player.send(ConfigMessage(1262, if (female) 8 else 1))
            }
            // head
            92, 93 -> {
                updateLook(player, Body.HEAD, buttonId == 93)
            }
            // beard
            97, 98 -> {
                updateLook(player, Body.FACIAL, buttonId == 98)
            }
            // torso
            341, 342 -> {
                updateLook(player, Body.TORSO, buttonId == 342)
            }
            // arms
            345, 346 -> {
                updateLook(player, Body.ARMS, buttonId == 346)
            }
            //hands
            349, 350 -> {
                updateLook(player, Body.HANDS, buttonId == 350)
            }
            // legs
            353, 354 -> {
                updateLook(player, Body.LEGS, buttonId == 354)
            }
            // feet
            357, 358 -> {
                updateLook(player, Body.FEET, buttonId == 358)
            }
            // accept design
            362 -> {
                player.interfaceSet.close()
//                var count = 0
//                Body.values().forEach { body ->
//                    val i = player.appearance.getBody(body)
//                    println("i=$i body=${body.name}")
//                }
//                count = 0
//                Colour.values().forEach { color ->
//                    val i = player.appearance.getColor(color)
//                    println("i=$i color=${color.name}")
//                }
            }

            169 -> {
                //randomise head
            }
            // randomise
            321 -> {
                player.appearance = generateAppearance()
                player.send(ConfigMessage(1260, 0)) //0 off 1 on
            }

            else -> {
//                player.sendMessage("buttonId = $buttonId")
//                player.sendMessage("SkinColor: ${player.appearance.getColor(Colour.SKIN)}")
            }
        }
    }
}

private fun generateAppearance(gender: Gender = if (Random.nextBoolean()) Gender.MALE else Gender.FEMALE): Appearance {
    return Appearance(
        gender, intArrayOf(
            Body.HEAD.getIds(gender).random(),
            Body.FACIAL.getIds(gender).random(),
            Body.TORSO.getIds(gender).random(),
            Body.ARMS.getIds(gender).random(),
            Body.HANDS.getIds(gender).random(),
            Body.LEGS.getIds(gender).random(),
            Body.FEET.getIds(gender).random()
        ),
        intArrayOf(2, 5, 8, 11, 14)
    )
}

private val charDesign = CharDesign()
val charDesignPlugin = pluginHandler(
    { event ->
        if (event is ButtonEvent) {
            charDesign.handleButtons(event.player, event.slotId)
        }
        if (event is LoginEvent) {
            // event.player.appearance = generateAppearance()
        }

    }, arrayOf(
        handleCommand("char") { player, args ->
            charDesign.open(player)
        },
        handleCommand("c") { player, args ->
            println(charDesign.getAttribute(player.username, "char-design:look0", 0))
        },
        handleCommand("gender") { player, args ->
            player.appearance =
                if (player.appearance.gender == Gender.MALE)
                    FEMALE_APPEARANCE else MALE_APPEARANCE
        },
        handleCommand("hair") { player, args ->
            if (args.size != 1) {
                player.sendMessage("Syntax ::hair [id]")
                return@handleCommand
            }
            val id = args[0].toInt()

            player.appearance = player.appearance.apply {
                setBody(Body.HEAD, id)
            }

            player.sendMessage("hair: id=$id")
        },
        handleCommand("r") { player, args ->
            player.settings.toggleTwoButtonMouse()
        },
        handleCommand("close") { player, args ->
            player.interfaceSet.close()
//            val value = player.getAttribute("HEAD", 0)
            //   val value = charDesign.getAttribute(player.username, "HEAD")
            //   println("Close: $value")
//            player.appearance.setBody(Body.HEAD, value)
        },
        handleCommand("gc") { player, args ->
            Body.values().forEach { body ->
                val i = player.appearance.getBody(body)
                println("style[${body.name}] = ${i}")
            }
            Colour.values().forEach { color ->
                val i = player.appearance.getColor(color)
//                println("style[${body.name}] = ${value}")
            }
        }
    ),
    arrayOf()
)

//val handlers = emptyArray<ButtonHandler>()
//val buttons = arrayOf(37, 40, 49, 52, 92, 93)
//val a = buttons.forEach { buttonId ->
//    handlers[buttonId] = handleButton(buttonId) { player, slot, parameter -> }
//}

//fun far far away quad() {
//    a.
//}

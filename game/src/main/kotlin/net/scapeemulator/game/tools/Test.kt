package net.scapeemulator.game.tools

import net.scapeemulator.game.model.Hit
import net.scapeemulator.game.model.HitType
import java.util.*


fun rwJson() {
//    val mapper = ObjectMapper()
//
//    //dump
//    mapper.enable(SerializationFeature.INDENT_OUTPUT)
//    mapper.writeValue(File("./data/components.json"), ComponentDefinition.getDefinitions())
//
//
//    //         //read
//    val map: MutableMap<Int, ItemDefinition> =
//        mapper.readValue(
//            File("./data/components.json"),
//            object : TypeReference<MutableMap<Int, ItemDefinition>>() {})

}

var hitQueue: Queue<Hit> = LinkedList()
fun main() {
    hitQueue = LinkedList()
    hitQueue.add(Hit(1, HitType.NONE))
    hitQueue.add(Hit(2, HitType.NORMAL))
    hitQueue.add(Hit(3, HitType.POISON))
    hitQueue.add(Hit(4, HitType.DISEASE))

    hitQueue.add(Hit(5, HitType.NONE))
    hitQueue.add(Hit(6, HitType.NORMAL))
    hitQueue.add(Hit(7, HitType.POISON))
    hitQueue.add(Hit(8, HitType.DISEASE))

    for (i in 0..3 step 1) {
        if (hitQueue.peek() == null) continue
        val hit = hitQueue.poll()
        println("Polling: ${hit.damage}")
        val secondary = i == 1
        if (secondary) {
            println("-----------Secondary: ${hit.damage}")
        } else {
            println("-----------Primary: ${hit.damage}")
        }
    }

}
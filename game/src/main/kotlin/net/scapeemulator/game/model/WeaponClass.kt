package net.scapeemulator.game.model

import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.game.model.AttackStyle.Bonus.*
import net.scapeemulator.game.model.AttackStyle.Style.*


class AttackStyle(val style: Style, val bonus: Bonus) {
    enum class Style {
        ACCURATE,
        AGGRESSIVE,
        CONTROLLED,
        DEFENSIVE,
        RANGE_ACCURATE,
        RANGE_RAPID,
        RANGE_LONG,
        CAST_DEFENSIVE,
        CAST;
    }

    enum class Bonus {
        BONUS_STAB,
        BONUS_SLASH,
        BONUS_CRUSH,
        BONUS_MAGIC,
        BONUS_RANGE, //range accurate
        BONUS_RANGE_RAPID,
        BONUS_RANGE_DEFENSIVE;
    }

    companion object {
        fun getStyle(style: Int) = Style.entries[style]
        fun getBonus(style: Int) = Bonus.entries[style]
    }
}

//attackStyles[settings.attackStyle]

enum class WeaponClass(val tab: Int, vararg val attackStyles: AttackStyle) {
    UNARMED(
        Interface.ATTACK_UNARMED,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_CRUSH)
    ),
    STAFF(
        Interface.ATTACK_STAFF,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_CRUSH),
        AttackStyle(CAST_DEFENSIVE, BONUS_MAGIC),
        AttackStyle(CAST, BONUS_MAGIC)
    ),
    AXE(
        Interface.ATTACK_AXE,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    SCEPTER(
        Interface.ATTACK_STAFF,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_CRUSH)
    ),
    PICKAXE(
        Interface.ATTACK_PICKAXE,
        AttackStyle(ACCURATE, BONUS_STAB),
        AttackStyle(AGGRESSIVE, BONUS_STAB),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_STAB)
    ),
    SWORD_DAGGER(
        Interface.ATTACK_DAGGER,
        AttackStyle(ACCURATE, BONUS_STAB),
        AttackStyle(AGGRESSIVE, BONUS_STAB),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(DEFENSIVE, BONUS_STAB)
    ),
    SCIMITAR(
        81,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(CONTROLLED, BONUS_STAB),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    GODSWORD(
        Interface.ATTACK_GODSWORD,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(CONTROLLED, BONUS_STAB),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    TWO_H_SWORD(
        Interface.ATTACK_SWORD,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    MACE(
        Interface.ATTACK_MACE,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(CONTROLLED, BONUS_STAB),
        AttackStyle(DEFENSIVE, BONUS_CRUSH)
    ),
    CLAWS(
        Interface.ATTACK_CLAWS,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(CONTROLLED, BONUS_STAB),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    MAUL(
        Interface.ATTACK_MAUL,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_CRUSH)
    ),
    WHIP(
        Interface.ATTACK_WHIP,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(CONTROLLED, BONUS_SLASH),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    FLOWERS(
        Interface.ATTACK_MAUL,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_CRUSH)
    ),
    MUD_PIE(
        Interface.ATTACK_THROWN,
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(RANGE_RAPID, BONUS_RANGE),
        AttackStyle(RANGE_LONG, BONUS_RANGE)
    ),
    SPEAR(
        Interface.ATTACK_SPEAR,
        AttackStyle(CONTROLLED, BONUS_STAB),
        AttackStyle(CONTROLLED, BONUS_SLASH),
        AttackStyle(CONTROLLED, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_STAB)
    ),
    HALBERD(
        Interface.ATTACK_HALBERD,
        AttackStyle(CONTROLLED, BONUS_STAB),
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(DEFENSIVE, BONUS_STAB)
    ),
    BOW(
        Interface.ATTACK_BOW,
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(RANGE_RAPID, BONUS_RANGE),
        AttackStyle(RANGE_LONG, BONUS_RANGE)
    ),
    CROSSBOW(
        Interface.ATTACK_LONGBOW,
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(RANGE_RAPID, BONUS_RANGE),
        AttackStyle(RANGE_LONG, BONUS_RANGE)
    ),
    THROWN(
        Interface.ATTACK_THROWN,
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(RANGE_RAPID, BONUS_RANGE),
        AttackStyle(RANGE_LONG, BONUS_RANGE)
    ),
    CHINCHOMPA(
        473,
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(RANGE_RAPID, BONUS_RANGE),
        AttackStyle(RANGE_LONG, BONUS_RANGE)
    ),
    FIXED_DEVICE(
        Interface.ATTACK_FIXED_DEVICE,
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH)
    ),
    SALAMANDER(
        474,
        AttackStyle(AGGRESSIVE, BONUS_SLASH),
        AttackStyle(RANGE_ACCURATE, BONUS_RANGE),
        AttackStyle(CAST_DEFENSIVE, BONUS_MAGIC)
    ),
    SCYTHE(
        Interface.ATTACK_SCYTHE,
        AttackStyle(ACCURATE, BONUS_SLASH),
        AttackStyle(AGGRESSIVE, BONUS_STAB),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_SLASH)
    ),
    IVANDIS_FLAIL(
        Interface.ATTACK_STAFF,
        AttackStyle(ACCURATE, BONUS_CRUSH),
        AttackStyle(AGGRESSIVE, BONUS_CRUSH),
        AttackStyle(DEFENSIVE, BONUS_CRUSH)
    );

    companion object {
        //tab, settings.attackStyle
        val logger = KotlinLogging.logger {}

        fun getClass(tab: Int): WeaponClass {
            //todo log unknown
            val find = entries.find { it.tab == tab }
            return find ?: UNARMED
        }

        fun getStyle(tab: Int, setting: Int): AttackStyle {
            return getClass(tab).attackStyles[setting]
        }
    }

}
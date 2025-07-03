//package net.scapeemulator.game.plugin
//
//import net.scapeemulator.game.model.Entity
//import net.scapeemulator.game.model.Player
//import net.scapeemulator.game.model.Skill
//import net.scapeemulator.game.msg.Audio
//import net.scapeemulator.game.msg.ConfigMessage
//import java.util.*
//
//class SkillBonus(val skillId: Int, val bonus: Double, val baseBonus: Int = 0)
//
//enum class PrayerCategory {
//    BABY_BLUE, GREEN, PINK, LIME_GREEN, ORANGE, PURPLE, DARK_GREEN, DARK_BROWN, LIGHT_BROWN, RED, MAGENTA
//}
//
//enum class PrayerType(
//    val level: Int,
//    val drain: Int,
//    val config: Int,
//    val button: Int,
//    restriction: PrayerCategory,
//    secondRestriction: PrayerCategory?,
//    audio: Audio,
//    vararg bonuses: SkillBonus
//) {
//    THICK_SKIN(1, 12, 83, 5, PrayerCategory.BABY_BLUE, 10000, SkillBonus(Skill.DEFENCE, 0.05)),
//    BURST_OF_STRENGTH(4, 12, 84, 7, PrayerCategory.GREEN, 2689, SkillBonus(Skill.STRENGTH, 0.05)),
//    CLARITY_OF_THOUGHT(7, 12, 85, 9, PrayerCategory.PINK, 2664, SkillBonus(Skill.ATTACK, 0.05)),
//    SHARP_EYE(8, 12, 862, 11, PrayerCategory.LIME_GREEN, 2685, SkillBonus(Skill.RANGED, 0.05)),
//    MYSTIC_WILL(9, 12, 863, 13, PrayerCategory.LIME_GREEN, 2670, SkillBonus(Skill.MAGIC, 0.05)),
//    ROCK_SKIN(10, 6, 86, 15, PrayerCategory.BABY_BLUE, 2684, SkillBonus(Skill.DEFENCE, 0.1)),
//    SUPERHUMAN_STRENGTH(13, 6, 87, 17, PrayerCategory.GREEN, 2689, SkillBonus(Skill.STRENGTH, 0.1)),
//    IMPROVED_REFLEXES(16, 6, 88, 19, PrayerCategory.PINK, 2662, SkillBonus(Skill.ATTACK, 0.1)),
//    RAPID_RESTORE(19, 26, 89, 21, PrayerCategory.PURPLE, 2679),
//    RAPID_HEAL(22, 18, 90, 23, PrayerCategory.PURPLE, 2678),
//    PROTECT_ITEMS(25, 18, 91, 25, PrayerCategory.DARK_GREEN, 1982),
//    HAWK_EYE(26, 6, 864, 27, PrayerCategory.LIME_GREEN, 2666, SkillBonus(Skill.RANGED, 0.1)),
//    MYSTIC_LORE(27, 6, 865, 29, PrayerCategory.LIME_GREEN, 2668, SkillBonus(Skill.MAGIC, 0.1)),
//    STEEL_SKIN(28, 3, 92, 31, PrayerCategory.BABY_BLUE, 2687, SkillBonus(Skill.DEFENCE, 0.15)),
//    ULTIMATE_STRENGTH(31, 3, 93, 33, PrayerCategory.GREEN, 2691, SkillBonus(Skill.STRENGTH, 0.15)),
//    INCREDIBLE_REFLEXES(34, 3, 94, 35, PrayerCategory.PINK, 2667, SkillBonus(Skill.ATTACK, 0.15)),
//    PROTECT_FROM_SUMMONING(35, 2, 1168, 53, PrayerCategory.DARK_BROWN, PrayerCategory.MAGENTA, Audio(-1)),
//    PROTECT_FROM_MAGIC(37, 3, 95, 37, PrayerCategory.LIGHT_BROWN, 2675),
//    PROTECT_FROM_MISSILES(40, 3, 96, 39, PrayerCategory.LIGHT_BROWN, 2677),
//    PROTECT_FROM_MELEE(43, 4, 97, 41, PrayerCategory.LIGHT_BROWN, 2676),
//    EAGLE_EYE(44, 3, 866, 43, PrayerCategory.LIME_GREEN, 2666, SkillBonus(Skill.RANGED, 0.15)),
//    MYSTIC_MIGHT(45, 3, 867, 45, PrayerCategory.LIME_GREEN, 2669, SkillBonus(Skill.MAGIC, 0.15)),
//    RETRIBUTION(46, 12, 98, 47, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, Audio(10000)),
//    REDEMPTION(49, 6, 99, 49, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, Audio(2678)),
//    SMITE(52, 2, 100, 51, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, Audio(2685)),
//    CHIVALRY(
//        60,
//        2,
//        1052,
//        55,
//        PrayerCategory.PINK,
//        1000,
//        SkillBonus(Skill.DEFENCE, 0.2),
//        SkillBonus(Skill.STRENGTH, 0.18),
//        SkillBonus(Skill.ATTACK, 0.15)
//    ),
//    PIETY(
//        70,
//        2,
//        1053,
//        57,
//        PrayerCategory.PINK,
//        1000,
//        SkillBonus(Skill.DEFENCE, 0.25),
//        SkillBonus(Skill.STRENGTH, 0.23),
//        SkillBonus(Skill.ATTACK, 0.2)
//    );
//
//    private val restriction: PrayerCategory = restriction
//    private val secondRestriction: PrayerCategory? = secondRestriction
//    private val audio: Audio = audio
//    private val bonuses: Array<out SkillBonus> = bonuses
//
//    /**
//     * Constructs a new `PrayerType` `Object`.
//     * @param level the level.
//     * @param drain the drain, represents the seconds until a drain.
//     * @param config the config value to represent on and off.
//     * @param button the button value to turn the prayer off and on.
//     * @param bonuses the skill bonuses for this type.
//     */
//    constructor(
//        level: Int,
//        drain: Int,
//        config: Int,
//        button: Int,
//        restriction: PrayerCategory,
//        AudioId: Int,
//        vararg bonuses: SkillBonus
//    ) : this(
//        level,
//        drain,
//        config,
//        button,
//        restriction,
//        null,
//        Audio(AudioId),
//        *bonuses
//    )
//
//    fun getRestriction(): PrayerCategory = restriction
//    fun getBonuses(): Array<out SkillBonus> = bonuses
//
//
//    /**
//     * Method used to check if the player has the required level to toggle this
//     * type.
//     * @param player the player.
//     * @return `True` if it is permitted.
//     */
//    fun permitted(player: Player): Boolean {
////        if (player.getSkills().getStaticLevel(Skill.PRAYER) < level) {
////            player.getAudioManager().send(2673)
////            player.getDialogueInterpreter().sendDialogue(
////                "You need a <col=08088A>Prayer level of " + level + " to use " + org.arios.tools.StringUtils.formatDisplayName(
////                    name.lowercase(
////                        Locale.getDefault()
////                    ).replace("_", " ")
////                ) + "."
////            )
////            return false
////        }
//        return true
//    }
//
//    /**
//     * Method used to check if we need to toggle a prayer on or off.
//     * @param player the player.
//     * @return `True` if toggled.
//     */
//    fun toggle(player: Player, on: Boolean): Boolean {
//        player.send(ConfigMessage(config, if (on) 1 else 0))
//        if (on) {
//            flag(player, this)
//            player.getPrayer().getActive().add(this)
//            player.getPrayer().getTask().init(player)
//            iconify(player, getIcon(player, this))
//            player.getAudioManager().send(getAudio())
//        } else {
//            player.getPrayer().getActive().remove(this)
//            findNextIcon(player)
//            if (player.getPrayer().getActive().isEmpty()) {
//                player.getPrayer().getTask().stop(player)
//                return true
//            }
//        }
//        return true
//    }
//
//    /**
//     * Method used to flag others prayers that cannot be toggled together.
//     * @param player the player.
//     */
//    fun flag(player: Player, type: PrayerType) {
//        val active: List<PrayerType> = player.getPrayer().getActive()
//        val remove = arrayOfNulls<PrayerType>(active.size + 10)
//        var index = 0
//        for (i in active.indices) {
//            if (active[i].getRestriction() == type.getRestriction() || active[i].getSecondRestriction() != null && type.getSecondRestriction() != null && active[i].getSecondRestriction() == type.getSecondRestriction()) {
//                remove[index++] = active[i]
//                continue
//            }
//            for (b in active[i].getBonuses()) for (bb in type.getBonuses()) {
//                if ((bb.skillId == b.skillId) || ((b.skillId == Skill.STRENGTH || b.skillId == Skill.ATTACK)
//                            && (bb.skillId == Skill.MAGIC || bb.skillId == Skill.RANGED)) || ((b.skillId == Skill.RANGED || b.skillId == Skill.MAGIC)
//                            && (bb.skillId == Skill.ATTACK || bb.skillId == Skill.STRENGTH)) || (b.skillId == Skill.DEFENCE && bb.skillId == Skill.DEFENCE)
//                ) remove[index++] = active[i]
//            }
//        }
//        for (i in 0 until index) remove[i]!!.toggle(player, false)
//    }
//
//    /**
//     * Method used to iconify the player.
//     * @param active the active list.
//     */
//    fun iconify(player: Player, icon: Int): Boolean {
//        if (icon == -1) return false
//        player.getAppearance().setHeadIcon(icon)
//        player.getAppearance().sync()
//        return false
//    }
//
//    /**
//     * Method used to find the next icon in place.
//     * @param player the player.
//     */
//    fun findNextIcon(player: Player) {
//        if (!hasIcon(player)) {
//            player.getAppearance().setHeadIcon(-1)
//            player.getAppearance().sync()
//        }
//        if ((this == PROTECT_FROM_MELEE || (this == PROTECT_FROM_MISSILES) || (this == PROTECT_FROM_MAGIC)) && player.getPrayer()
//                .get(PROTECT_FROM_SUMMONING)
//        ) {
//            iconify(player, 7)
//        } else if (this == PROTECT_FROM_SUMMONING) {
//            for (t in player.getPrayer().getActive())
//                iconify(player, getIcon(player, t))
//
//            if (player.getAppearance().getHeadIcon() == 7) {
//                player.getAppearance().setHeadIcon(-1)
//                player.getAppearance().sync()
//            }
//        }
//    }
//
//    /**
//     * Method used to get the icon value.
//     * @param active the list prayers active.
//     * @return the icon.
//     */
//    fun getIcon(player: Player, type: PrayerType): Int {
//        val active: List<PrayerType> = player.getPrayer().getActive()
//        for (i in ICON_CACHE.indices) {
//            if (ICON_CACHE[i].size == 2 && type == (ICON_CACHE[i][0] as PrayerType)) {
//                return ICON_CACHE[i][1] as Int
//            } else if (ICON_CACHE[i].size == 3 && type == (ICON_CACHE[i][0] as PrayerType)) {
//                if (active.contains(PROTECT_FROM_SUMMONING)) return ICON_CACHE[i][2] as Int
//                return ICON_CACHE[i][1] as Int
//            } else if (ICON_CACHE[i].size == 8 && type == (ICON_CACHE[i][0] as PrayerType)) {
//                for (k in 2 until ICON_CACHE[i].size) if (active.contains(ICON_CACHE[i][k])) return ICON_CACHE[i][k + 1] as Int
//                return ICON_CACHE[i][1] as Int
//            }
//        }
//        return -1
//    }
//
//    /**
//     * Method used to check if theres an icon present.
//     * @param player the player.
//     * @return `True` if theres an icon present.
//     */
//    fun hasIcon(player: Player): Boolean {
//        var count = 0
//        for (type in player.getPrayer().getActive())
//            if (getIcon(player, type) != -1) count++
//        return count != 0
//    }
//
//    fun getSecondRestriction(): PrayerCategory? = secondRestriction
//    fun getAudio(): Audio = audio
//
//    companion object {
//        /**
//         * Represents the a cache of objects related to prayers in order to decide
//         * what head icon to display.
//         */
//        private val ICON_CACHE = arrayOf(
//            arrayOf<Any>(REDEMPTION, 5),
//            arrayOf<Any>(RETRIBUTION, 3),
//            arrayOf<Any>(SMITE, 4),
//            arrayOf<Any>(PROTECT_FROM_MAGIC, 2, 10),
//            arrayOf<Any>(PROTECT_FROM_MELEE, 0, 8),
//            arrayOf<Any>(PROTECT_FROM_MISSILES, 1, 9),
//            arrayOf<Any>(
//                PROTECT_FROM_SUMMONING, 7,
//                PROTECT_FROM_MELEE, 8,
//                PROTECT_FROM_MISSILES, 9,
//                PROTECT_FROM_MAGIC, 10
//            )
//        )
//
//        /**
//         * Method used to return the type by the button.
//         * @param button the button.
//         * @return the type.
//         */
//        fun get(button: Int): PrayerType? {
//            for (type in entries) if (type.button == button) return type
//            return null
//        }
//
//        val meleeTypes: Array<PrayerType?> get() = getByBonus(Skill.ATTACK, Skill.STRENGTH)
//        val rangeTypes: Array<PrayerType?> get() = getByBonus(Skill.RANGED)
//        val magicTypes: Array<PrayerType?> get() = getByBonus(Skill.MAGIC)
//
//        private fun getByBonus(vararg ids: Int): Array<PrayerType?> {
//            val types = arrayOfNulls<PrayerType>(entries.size)
//            var count = 0
//            for (type in entries) for (b in type.getBonuses())
//                for (i in ids) if (i == b.skillId) {
//                    types[count] = type
//                    count++
//                }
//            return types
//        }
//    }
//}
//
//
//class Prayer(player: Player) {
//
//    private val active: MutableList<PrayerType> = ArrayList<PrayerType>()
//
//    val task: DrainTask = DrainTask()
//
//
//    private val player: Player = player
//
//
//    fun toggle(type: PrayerType): Boolean {
//        if (!permitted(type)) return false
//
//        return type.toggle(player, !active.contains(type))
//    }
//
//    /**
//     * Method used to reset this prayer managers cached prayers.
//     */
//    fun reset() {
//        for (type in getActive()) {
//            player.getConfigManager().set(type.getConfig(), 0)
//        }
//        getActive().clear()
//        player.getAppearance().setHeadIcon(-1)
//        player.getAppearance().sync()
//    }
//
//    /**
//     * Starts the redemption effect.
//     */
//    fun startRedemption() {
//        player.getAudioManager().send(2681)
//        player.graphics(Graphics.create(436))
//        player.getSkills().heal((player.getSkills().getStaticLevel(org.arios.game.content.skill.Skills.PRAYER) * 0.25).toInt())
//        player.getSkills().setPrayerPoints(0.0)
//        reset()
//    }
//
//    /**
//     * Starts the retribution effect.
//     * @param killer The entity who killed this player.
//     */
//    fun startRetribution(killer: Entity) {
//        val l: Location = player.getLocation()
//        for (x in -1..1) {
//            for (y in -1..1) {
//                if (x != 0 || y != 0) {
//                  Projectile.create(l, l.transform(x, y, 0), 438, 0, 0, 10, 20, 0, 11).send()
//                }
//            }
//        }
//        player.getAudioManager().send(159)
//        player.graphics(Graphics.create(437))
//        val maximum: Int =
//            (player.getSkills().getStaticLevel(org.arios.game.content.skill.Skills.PRAYER) * 0.25).toInt() - 1
//        if (killer !== player && killer.getLocation().withinDistance(player.getLocation(), 1)) {
//            killer.getImpactHandler().manualHit(
//                player,
//                1 + org.arios.tools.RandomFunction.randomize(maximum),
//                combat.ImpactHandler.HitsplatType.NORMAL
//            )
//        }
//        if (player.getProperties().isMultiZone()) {
//            var targets: List<*>? = null
//            targets = if (killer is NPC) {
//                RegionManager.getSurroundingNPCs(player, player, killer)
//            } else {
//                RegionManager.getSurroundingPlayers(player, player, killer)
//            }
//            for (o in targets) {
//                val entity: Entity = o as Entity
//                if (entity.isAttackable(player, combat.CombatStyle.MAGIC)) {
//                    entity.getImpactHandler().manualHit(
//                        player,
//                        1 + org.arios.tools.RandomFunction.randomize(maximum),
//                        combat.ImpactHandler.HitsplatType.NORMAL
//                    )
//                }
//            }
//        }
//    }
//
//    /**
//     * Gets the skill bonus for the given skill id.
//     * @param skillId The skill id.
//     * @return The bonus for the given skill.
//     */
//    fun getSkillBonus(skillId: Int): Double {
//        var bonus = 0.0
//        for (type in active) {
//            for (b in type.getBonuses()) {
//                if (b.getSkillId() == skillId) {
//                    bonus += b.getBonus()
//                }
//            }
//        }
//        return bonus
//    }
//
//    /**
//     * Method used to check if we're permitted to toggle this prayer.
//     * @param type the type.
//     * @return `True` if permitted to be toggled.
//     */
//    private fun permitted(type: PrayerType): Boolean {
//        return player.getSkills().getPrayerPoints() > 0 && type.permitted(player)
//    }
//
//    /**
//     * Method used to return value of `True` if the [.active]
//     * prayers contains the prayer type.
//     * @param type the type of prayer.
//     * @return `True` if so.
//     */
//    fun get(type: PrayerType): Boolean {
//        return active.contains(type)
//    }
//
//    /**
//     * Gets the player.
//     * @return The player.
//     */
//    fun getPlayer(): Player {
//        return player
//    }
//
//    /**
//     * Gets the active prayers.
//     * @return The active.
//     */
//    fun getActive(): MutableList<PrayerType> {
//        return active
//    }
//}
//
//internal val plugin = PluginHandler({}, { pluginEvent -> })
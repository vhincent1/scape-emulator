//package net.scapeemulator.game.pathfinder
//
//import net.scapeemulator.game.model.Direction
//import net.scapeemulator.game.model.Position
//import kotlin.math.abs
//
//class DumbPathFinder : PathFinder {
//    fun find(
//        position: Position,
//        dest: Position,
//        size: Int,
//        max: Int,
//        inside: Boolean
//    ): Path {
//        val path: Path = Path()
//        var p: Position? = position
//        for (i in 0 until max) {
//            p = bestDummyPosition(p, dest, size)
//            if (p != null) {
//                if (!inside && p == dest) {
//                    val cur = if (path.isEmpty) {
//                        position
//                    } else {
//                        path.points.last
//                    }
//                    when (Direction.between(cur, dest)) {
//                        Direction.NONE, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST -> {}
//                        Direction.NORTH_EAST -> if (Direction.isTraversable(
//                                cur,
//                                Direction.NORTH,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x, cur.y + 1, cur.height))
//                        } else if (Direction.isTraversable(
//                                cur,
//                                Direction.EAST,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x + 1, cur.y, cur.height))
//                        }
//
//                        Direction.NORTH_WEST -> if (Direction.isTraversable(
//                                cur,
//                                Direction.NORTH,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x, cur.y + 1, cur.height))
//                        } else if (Direction.isTraversable(
//                                cur,
//                                Direction.WEST,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x - 1, cur.y, cur.height))
//                        }
//
//                        Direction.SOUTH_EAST -> if (Direction.isTraversable(
//                                cur,
//                                Direction.SOUTH,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x, cur.y - 1, cur.height))
//                        } else if (Direction.isTraversable(cur, Direction.EAST, size)) {
//                            path.addLast(Position(cur.x + 1, cur.y, cur.height))
//                        }
//
//                        Direction.SOUTH_WEST -> if (Direction.isTraversable(
//                                cur,
//                                Direction.SOUTH,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x, cur.y - 1, cur.height))
//                        } else if (Direction.isTraversable(
//                                cur,
//                                Direction.WEST,
//                                size
//                            )
//                        ) {
//                            path.addLast(Position(cur.x - 1, cur.y, cur.height))
//                        }
//                    }
//                    return path
//                }
//                path.addLast(p)
//            } else {
//                return path
//            }
//        }
//        return path
//    }
//
//    fun bestDummyPosition(cur: Position, next: Position, size: Int): Position? {
//        if (cur == next) {
//            return null
//        }
//        val deltaX: Int = next.x - cur.x
//        val deltaY: Int = next.y - cur.y
//        when (Direction.between(cur, next)) {
//            Direction.NONE -> return null
//            Direction.NORTH -> return Position(
//                cur.x,
//                cur.y + 1,
//                cur.height
//            )
//
//            Direction.SOUTH -> return Position(
//                cur.x,
//                cur.y - 1,
//                cur.height
//            )
//
//            Direction.EAST -> return Position(
//                cur.x + 1,
//                cur.y,
//                cur.height
//            )
//
//            Direction.WEST -> return Position(
//                cur.x - 1,
//                cur.y,
//                cur.height
//            )
//
//            Direction.NORTH_EAST -> {
//                if (Direction.isTraversable(
//                        cur,
//                        Direction.NORTH_EAST,
//                        size
//                    )
//                ) {
//                    return Position(cur.x + 1, cur.y + 1, cur.height)
//                }
//                return if (abs(deltaX.toDouble()) > abs(deltaY.toDouble())) {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.EAST,
//                            size
//                        )
//                    ) {
//                        Position(cur.x + 1, cur.y, cur.height)
//                    } else {
//                        Position(cur.x, cur.y + 1, cur.height)
//                    }
//                } else {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.NORTH,
//                            size
//                        )
//                    ) {
//                        Position(cur.x, cur.y + 1, cur.height)
//                    } else {
//                        Position(cur.x + 1, cur.y, cur.height)
//                    }
//                }
//            }
//
//            Direction.NORTH_WEST -> {
//                if (Direction.isTraversable(
//                        cur,
//                        Direction.NORTH_WEST,
//                        size
//                    )
//                ) {
//                    return Position(cur.x - 1, cur.y + 1, cur.height)
//                }
//                return if (abs(deltaX.toDouble()) > abs(deltaY.toDouble())) {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.WEST,
//                            size
//                        )
//                    ) {
//                        Position(cur.x - 1, cur.y, cur.height)
//                    } else {
//                        Position(cur.x, cur.y + 1, cur.height)
//                    }
//                } else {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.NORTH,
//                            size
//                        )
//                    ) {
//                        Position(cur.x, cur.y + 1, cur.height)
//                    } else {
//                        Position(cur.x - 1, cur.y, cur.height)
//                    }
//                }
//            }
//
//            Direction.SOUTH_EAST -> {
//                if (Direction.isTraversable(
//                        cur,
//                        Direction.SOUTH_EAST,
//                        size
//                    )
//                ) {
//                    return Position(cur.x + 1, cur.y - 1, cur.height)
//                }
//                return if (abs(deltaX.toDouble()) > abs(deltaY.toDouble())) {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.EAST,
//                            size
//                        )
//                    ) {
//                        Position(cur.x + 1, cur.y, cur.height)
//                    } else {
//                        Position(cur.x, cur.y - 1, cur.height)
//                    }
//                } else {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.SOUTH,
//                            size
//                        )
//                    ) {
//                        Position(cur.x, cur.y - 1, cur.height)
//                    } else {
//                        Position(cur.x + 1, cur.y, cur.height)
//                    }
//                }
//            }
//
//            Direction.SOUTH_WEST -> {
//                if (Direction.isTraversable(
//                        cur,
//                        Direction.SOUTH_WEST,
//                        size
//                    )
//                ) {
//                    return Position(cur.x - 1, cur.y - 1, cur.height)
//                }
//                return if (abs(deltaX.toDouble()) > abs(deltaY.toDouble())) {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.WEST,
//                            size
//                        )
//                    ) {
//                        Position(cur.x - 1, cur.y, cur.height)
//                    } else {
//                        Position(cur.x, cur.y - 1, cur.height)
//                    }
//                } else {
//                    if (Direction.isTraversable(
//                            cur,
//                            Direction.SOUTH,
//                            size
//                        )
//                    ) {
//                        Position(cur.x, cur.y - 1, cur.height)
//                    } else {
//                        Position(cur.x - 1, cur.y, cur.height)
//                    }
//                }
//            }
//        }
//        return null
//    }
//}
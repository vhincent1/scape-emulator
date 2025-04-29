package net.scapeemulator.game.util.math

object PerlinNoise {
    @JvmStatic
    fun tileHeight(x: Int, y: Int): Int {
        var total = 0

        /* 3 octaves */
        total += interpolatedNoise(x + 45365, y + 91923, 4) - 128
        total += (interpolatedNoise(x + 10294, y + 37821, 2) - 128) / 2
        total += (interpolatedNoise(x, y, 1) - 128) / 4

        total = (total * 0.3).toInt() + 35

        return if (total < 10) 10
        else if (total > 60) 60
        else total
    }

    private fun interpolate(a: Int, b: Int, t: Int, freqReciprocal: Int): Int {
        val cosine = 65536 - Trigonometry.COSINE[t * Trigonometry.COSINE.size / (2 * freqReciprocal)] / 2
        return (a * (65536 - cosine)) / 65536 + (b * cosine) / 65536
    }

    private fun interpolatedNoise(x: Int, y: Int, freqReciprocal: Int): Int {
        var x = x
        var y = y
        val xt = x % freqReciprocal
        val yt = y % freqReciprocal

        x /= freqReciprocal
        y /= freqReciprocal

        val v1 = smoothNoise(x, y)
        val v2 = smoothNoise(x + 1, y)
        val v3 = smoothNoise(x, y + 1)
        val v4 = smoothNoise(x + 1, y + 1)

        val i1 = interpolate(v1, v2, xt, freqReciprocal)
        val i2 = interpolate(v3, v4, xt, freqReciprocal)

        return interpolate(i1, i2, yt, freqReciprocal)
    }

    private fun smoothNoise(x: Int, y: Int): Int {
        val corners = noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, y + 1) + noise(x + 1, y + 1)
        val sides = noise(x - 1, y) + noise(x + 1, y) + noise(x, y - 1) + noise(x, y + 1)
        val center = noise(x, y)
        return corners / 16 + sides / 8 + center / 4
    }

    private fun noise(x: Int, y: Int): Int {
        var n = x + y * 57
        n = (n shl 13) xor n
        n = (n * (n * n * 15731 + 789221) + 1376312589) and 0x7fffffff
        return (n shr 19) and 0xff
    }
}

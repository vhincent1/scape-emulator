package net.scapeemulator.game.net.file

import io.netty.channel.DefaultFileRegion
import io.netty.channel.FileRegion
import java.io.File
import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.util.regex.Pattern

class FileProvider(private val codeOnly: Boolean) {
    @Throws(IOException::class)
    fun serve(path: String): FileRegion? {
        var path = path
        path = rewrite(path)
        if (codeOnly && !path.matches("^/(jogl_\\d_\\d\\.lib|(loader|loader_gl|runescape)\\.jar|(jogl|runescape|runescape_gl)\\.pack200|unpackclass.pack)$".toRegex())) return null

        val f = File(root, path)
        if (!f.absolutePath.startsWith(root.absolutePath)) return null

        if (!f.exists() || !f.isFile()) return null


        return DefaultFileRegion(FileChannel.open(f.toPath(), StandardOpenOption.READ), 0, f.length())
    }

    private fun rewrite(path: String): String {
        var pattern = Pattern.compile("^/jogl_(\\d)_(\\d)_-?\\d+\\.lib$")
        var matcher = pattern.matcher(path)
        if (matcher.matches()) {
            return "/jogl_" + matcher.group(1) + "_" + matcher.group(2) + ".lib"
        }

        pattern = Pattern.compile("^/(jogl|runescape|runescape_gl)_-?\\d+\\.pack200$")
        matcher = pattern.matcher(path)
        if (matcher.matches()) {
            return "/" + matcher.group(1) + ".pack200"
        }

        pattern = Pattern.compile("^/(loader|loader_gl|runescape)_-?\\d+\\.jar$")
        matcher = pattern.matcher(path)
        if (matcher.matches()) {
            return "/" + matcher.group(1) + ".jar"
        }

        if (path.matches("^/unpackclass_-?\\d+\\.pack$".toRegex())) {
            return "/unpackclass.pack"
        }

        if (path == "/") {
            return "/index.html"
        }

        return path
    }

    companion object {
        private val root = File("data/www/")
    }
}

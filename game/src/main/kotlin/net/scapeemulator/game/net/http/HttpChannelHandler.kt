package net.scapeemulator.game.net.http

import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundMessageHandlerAdapter
import io.netty.channel.FileRegion
import io.netty.handler.codec.http.*
import net.scapeemulator.game.net.file.FileProvider
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*


/*
TODO:
https://github.com/Rune-Status/rune-js-server

Online players can be polled via the REST protocol for web applications.

API Endpoints:
GET /players : Returns a list of players currently logged into the game server
GET /items?page=x&limit=y : Returns a list of item metadata loaded by the game server
GET /items/{itemId} : Returns details about a specific item by id
PUT /items/{itemId} : Updates an item's configurable server data
*/

class HttpChannelHandler : ChannelInboundMessageHandlerAdapter<HttpRequest>() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info { "Channel connected: " + ctx.channel().remoteAddress() + "." }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        logger.info { "Channel disconnected: " + ctx.channel().remoteAddress() + "." }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
        logger.warn(cause) { "Exception caught, closing channel..." }
        ctx.close()
    }

    fun formatParams(request: HttpRequest): StringBuilder {
        val responseData = StringBuilder()
        val queryStringDecoder = QueryStringDecoder(request.uri)
        val params = queryStringDecoder.parameters()
        if (!params.isEmpty()) {
            for (p in params.entries) {
                val key: String = p.key!!
                val vals: MutableList<String> = p.value
                for (`val` in vals) {
                    responseData.append("Parameter: ").append(key.uppercase(Locale.getDefault())).append(" = ")
                        .append(`val`.uppercase(Locale.getDefault())).append("\r\n")
                }

            }
            responseData.append("\r\n")
        }
        return responseData
    }

    @Throws(IOException::class)
    override fun messageReceived(ctx: ChannelHandlerContext, request: HttpRequest) {
        val path = request.uri
        val file: FileRegion? = provider.serve(path)

        val responseData = StringBuilder()
        val queryStringDecoder = QueryStringDecoder(request.uri)
        val params = queryStringDecoder.parameters()
        params.forEach { key, value -> responseData.append("$key: $value\r\n") }
        println("path: " + path.split('/'))
        println(responseData)
        when (path) {
            "/test/test" -> {

            }
        }
        if (path == "/test") {
            responseData.append("hi $params")
            val players = 1//World.world.players.size
            val buf = Unpooled.wrappedBuffer("online: $players\n$responseData".toByteArray(StandardCharsets.UTF_8))
            val response: HttpResponse = DefaultHttpResponse(request.protocolVersion, HttpResponseStatus.OK)
            response.headers().set("Server", "JAGeX/3.1")
            response.headers().set("Connection", "close")
            response.headers().set("Content-Type", "text/html; charset=utf-8")
            response.headers().set("Content-Length", buf.readableBytes())
            ctx.write(response)
            ctx.write(buf).addListener(ChannelFutureListener.CLOSE)
            return
        }
        if (file != null) {
            val response: HttpResponse = DefaultHttpResponse(request.protocolVersion, HttpResponseStatus.OK)
            response.headers().set("Server", "JAGeX/3.1")
            response.headers().set("Connection", "close")
            response.headers().set("Content-Type", getMimeType(path))
            response.headers().set("Content-Length", file.count())
            ctx.write(response)
            ctx.write(file).addListener(ChannelFutureListener.CLOSE)
        } else {
            val buf = Unpooled.wrappedBuffer(NOT_FOUND_HTML.toByteArray(StandardCharsets.UTF_8))
            val response: HttpResponse = DefaultHttpResponse(request.protocolVersion, HttpResponseStatus.NOT_FOUND)
            response.headers().set("Server", "JAGeX/3.1")
            response.headers().set("Connection", "close")
            response.headers().set("Content-Type", "text/html; charset=utf-8")
            response.headers().set("Content-Length", buf.readableBytes())
            ctx.write(response)
            ctx.write(buf).addListener(ChannelFutureListener.CLOSE)
        }
    }

    private fun getMimeType(path: String): String {
        if (path == "/test" || path.endsWith(".html")) return "text/html; charset=utf-8"
        if (path == "/" || path.endsWith(".html")) return "text/html; charset=utf-8"
        else if (path.endsWith(".jar")) return "application/java-archive"
        else if (path.endsWith("pack200")) return "application/x-java-pack200"
        else return "application/octet-stream"
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private const val NOT_FOUND_HTML =
            "<html><head><title>404 - Page not found</title></head><body style=\"color: black; background: white; font-family: Arial, Verdana, Helvetica;\"><div style=\"font-weight: bold; color: #666666; font-size: large\">404 - Page not found</div><hr width=\"300\" align=\"left\" /><p>Sorry, the page you were looking for was not found.</p><!--Padding for IEPadding Padding for IEPadding Padding for IEPadding Padding for IEPadding for IEPadding for IEPadding for IEPadding for IEPadding for IEPadding for IEPadding for IEPadding for IEPadding for IE--></body></html>"
        private val provider = FileProvider(false)
    }
}

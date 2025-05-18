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
    //todo: connection limits
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

    fun extractPathVariables(path: String, request: HttpRequest) {
        val uri: String = request.uri
        val segments: Array<String?> = uri.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (segment in segments) {
            println("s $segment")
        }
        println("value: " + segments[3])
        println("Path: $path")

        if (path.startsWith("/api/world/")) {
            println("Test")
            val apiName = segments[2]
            println("ApiName $apiName ${segments.size}")
            if (request.method == HttpMethod.GET && segments.size == 4 && apiName == "world") {
                val value = segments[3]
                println("World: $value")
            }
        }

//        if (request.method === HttpMethod.GET && segments.size == 3 && segments[1] == "world") {
//            val userId = segments[2]
//            println("User ID: " + userId)
//        } else if (request.method === HttpMethod.GET && segments.size == 4 && segments[1] == "products") {
//            val productId = segments[2]
//            val categoryId = segments[3]
//            println("Product ID: " + productId)
//            println("Category ID: " + categoryId)
//        } else {
//            println("No path variables found or invalid route.")
//        }
    }

    @Throws(IOException::class)
    override fun messageReceived(ctx: ChannelHandlerContext, request: HttpRequest) {
        val path = request.uri
        val file: FileRegion? = provider.serve(path)

        val responseData = StringBuilder()
        val queryStringDecoder = QueryStringDecoder(request.uri)
        val params = queryStringDecoder.parameters()
        params.forEach { key, value ->
            if (value != null)
                responseData.append("$key: $value\r\n")
        }


        extractPathVariables(path, request)
//        val decoder: QueryStringDecoder = QueryStringDecoder(request.uri)
//        val params = decoder.parameters()
//        // Access parameter values
//        if (params.containsKey("paramName")) {
//            val paramValues = params.get("paramName")
//            // Process the parameter values
//        }
//        // Example: Print all parameters
//        for (entry in params.entries) {
//            println("Parameter: " + entry.key + ", Values: " + entry.value)
//        }

        println("uri:  " + request.uri)
        println("path: " + path.split('/'))
//        println(responseData)
        when (path) {
            "/test/test" -> {

            }
        }//encode password parameter

        if (request.method == HttpMethod.POST) {

        }
        println("request method :" + request.method)
        if (path == "/api/world/") {
            responseData.append("hi $params")
            val players = 1//World.world.players.size
            //todo apikey?=
            val content: String = "World "
            val buf = Unpooled.wrappedBuffer(content.toByteArray(StandardCharsets.UTF_8))
            val response: HttpResponse = DefaultHttpResponse(request.protocolVersion, HttpResponseStatus.OK)
            response.headers().set("Server", "JAGeX/3.1")
            response.headers().set("Connection", "close")
            response.headers().set("Content-Type", "text/html; charset=utf-8")
            response.headers().set("Content-Length", buf.readableBytes())
            response.headers()
            ctx.write(response)
            ctx.write(buf).addListener(ChannelFutureListener.CLOSE)
            return
        }
        if (path == "/test") {
            responseData.append("hi $params")
            val players = 1//World.world.players.size
            //todo apikey?=
            val buf = Unpooled.wrappedBuffer("online: $players\n$responseData".toByteArray(StandardCharsets.UTF_8))
            val response: HttpResponse = DefaultHttpResponse(request.protocolVersion, HttpResponseStatus.OK)
            response.headers().set("Server", "JAGeX/3.1")
            response.headers().set("Connection", "close")
            response.headers().set("Content-Type", "text/html; charset=utf-8")
            response.headers().set("Content-Length", buf.readableBytes())
            response.headers()
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

fun decodeQueryString(url: String): Pair<String, Map<String, List<String>>> {
    val parts = url.split("?", limit = 2)
    val path = parts[0]
    val query = if (parts.size > 1) parts[1] else ""

    val params = query.split("/")
        .filter { it.isNotEmpty() }
        .map {
            val keyValue = it.split("/", limit = 2)
            val key = keyValue[0]
            val value = if (keyValue.size > 1) keyValue[1] else ""
            key to value
        }
        .groupBy({ it.first }, { it.second })

    return Pair(path, params)
}


fun main() {
    val (param, value) = decodeQueryString("/api/world/1")
    println("param $param = $value")

    val uri = "/api/world/1"

    val responseData = StringBuilder()
    val queryStringDecoder = QueryStringDecoder("/api/world/1")
    val params = queryStringDecoder.parameters()
    params.forEach { key, value ->
        if (value != null)
            responseData.append("$key: $value\r\n")
        println(key)
    }
    println(uri.split('/'))
}
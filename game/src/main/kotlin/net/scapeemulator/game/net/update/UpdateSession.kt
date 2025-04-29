package net.scapeemulator.game.net.update

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.handler.timeout.ReadTimeoutHandler
import net.scapeemulator.cache.Container
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.Session
import java.io.IOException
import java.util.*

class UpdateSession(server: GameServer, channel: Channel) : Session(server, channel) {
    private val service: UpdateService
    private val fileQueue: Deque<FileRequest> = ArrayDeque<FileRequest>()
    private var idle = true
    private var handshakeComplete = false

    init {
        this.service = server.updateService
    }

    fun processFileQueue() {
        val request: FileRequest?//check

        synchronized(fileQueue) {
            request = fileQueue.pop()
            if (fileQueue.isEmpty()) {
                idle = true
            } else {
                service.addPendingSession(this)
                idle = false
            }
        }

        if (request != null) {
            val type = request.type
            val file = request.file

            val cache = server.cache
            var buf: ByteBuf

            try {
                if (type == 255 && file == 255) {
                    val table = server.checksumTable
                    val container = Container(Container.COMPRESSION_NONE, table.encode())
                    buf = Unpooled.wrappedBuffer(container.encode())
                } else {
                    buf = Unpooled.wrappedBuffer(cache.store.read(type, file))
                    if (type != 255) buf = buf.slice(0, buf.readableBytes() - 2)
                }
                channel.write(FileResponse(request.isPriority, type, file, buf))
            } catch (ex: IOException) {
                logger.warn(ex) { "Failed to service file request $type, $file." }
            }
        }
    }

    override fun messageReceived(message: Any) {
        if (handshakeComplete) {
            if (message is FileRequest) {
                val request = message

                synchronized(fileQueue) {
                    if (request.isPriority) {
                        fileQueue.addFirst(request)
                    } else {
                        fileQueue.addLast(request)
                    }
                    if (idle) {
                        service.addPendingSession(this)
                        idle = false
                    }
                }
            } else if (message is UpdateEncryptionMessage) {
                val encryption = message
                val encoder = channel.pipeline().get(XorEncoder::class.java)
                encoder.setKey(encryption.key)
            }
        } else {
            val version = message as UpdateVersionMessage

            val status: Int
            if (version.version == server.version) {
                status = UpdateStatusMessage.STATUS_OK
            } else {
                status = UpdateStatusMessage.STATUS_OUT_OF_DATE
            }

            val future = channel.write(UpdateStatusMessage(status))
            if (status == UpdateStatusMessage.STATUS_OK) {
                /* the client won't re-connect so an on-demand session cannot time out */
                channel.pipeline().remove(ReadTimeoutHandler::class.java)
                handshakeComplete = true
            } else {
                future.addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    companion object {
        private val logger = InlineLogger(UpdateSession::class)
    }
}

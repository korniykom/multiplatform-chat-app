package com.korniykom.kotlin_chat.infra.storage

import com.korniykom.kotlin_chat.type.UserId
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO


@Service
class StorageService {

    private val rootDir = Paths.get("uploads/avatars")

    fun uploadAvatar(userId: UserId, file: MultipartFile): String {
        validate(file)

        Files.createDirectories(rootDir)

        val filename = "$userId-${System.currentTimeMillis()}.png"
        val targetPath = rootDir.resolve(filename)

        val original = ImageIO.read(file.inputStream)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image")

        val cropped = cropCenterSquare(original)
        val resized = resize(cropped, 256, 256)

        ImageIO.write(resized, "png", targetPath.toFile())

        return "/uploads/avatars/$filename"
    }

    fun deleteAvatar(profilePictureUrl: String) {
        val filename = Paths.get(profilePictureUrl).fileName ?: return
        Files.deleteIfExists(rootDir.resolve(filename))
    }

    private fun validate(file: MultipartFile) {
        if (file.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file")
        }

        if (file.contentType?.startsWith("image/") != true) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image type")
        }

        if (file.size > 2_000_000) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File too large")
        }
    }

    private fun cropCenterSquare(src: BufferedImage): BufferedImage {
        val size = minOf(src.width, src.height)
        val x = (src.width - size) / 2
        val y = (src.height - size) / 2
        return src.getSubimage(x, y, size, size)
    }

    private fun resize(src: BufferedImage, width: Int, height: Int): BufferedImage {
        val resized = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = resized.createGraphics()

        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        )
        g.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY
        )
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        g.drawImage(src, 0, 0, width, height, null)
        g.dispose()

        return resized
    }
}
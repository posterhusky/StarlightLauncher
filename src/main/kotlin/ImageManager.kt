package net.vanolex

import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.image.RescaleOp
import javax.imageio.ImageIO

object ImageManager {
    val backgroundClear = run {
        val customBg = loadFile("background.png")
        if (customBg.exists()) ImageIO.read(customBg)
        else ImageIO.read(javaClass.getResourceAsStream("/img/background.png"))
    }
    val backgroundBlur = shadeBackground(backgroundClear)

    var scaledBackgroundClear = scaleToCover(backgroundClear)
    var scaledBackgroundBlur = scaleToCover(backgroundBlur)

    val loadingSpinner: BufferedImage by lazy { ImageIO.read(javaClass.getResourceAsStream("/img/loading-spinner.png")) }

    var isFastRender = false

    fun rescaleBackground() {
        scaledBackgroundClear = scaleToCover(backgroundClear)
        scaledBackgroundBlur = scaleToCover(backgroundBlur)
    }

    fun scaleToCover(src: BufferedImage): BufferedImage {
        val bw = src.width.toDouble()
        val bh = src.height.toDouble()
        val ww = Window.width.toDouble()
        val wh = Window.height.toDouble()

        // Determine scale factor (cover mode)
        val scale = if (ww / wh > bw / bh) {
            ww / bw
        } else {
            wh / bh
        }

        // Compute translation to center the scaled image
        val tx = (ww - bw * scale) / 2.0
        val ty = (wh - bh * scale) / 2.0

        // Create target image of window size
        val result = BufferedImage(Window.width, Window.height, BufferedImage.TYPE_INT_ARGB)
        val g2 = result.createGraphics().apply {
            // Enable bilinear interpolation
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        }

        // Draw the scaled image, centered and cropped to the window bounds
        g2.drawImage(
            src,
            tx.toInt(),                // destination x
            ty.toInt(),                // destination y
            (bw * scale).toInt(),      // destination width
            (bh * scale).toInt(),      // destination height
            null
        )

        g2.dispose()
        return result
    }

    fun shadeBackground(bg: BufferedImage): BufferedImage {
        val radius = 20
        val w = bg.width
        val h = bg.height
        val pix = IntArray(w * h)
        bg.getRGB(0, 0, w, h, pix, 0, w)

        // Temporary arrays for the blur passes
        val r = IntArray(pix.size)
        val g = IntArray(pix.size)
        val b = IntArray(pix.size)
        val resultPix = IntArray(pix.size)

        val windowSize = radius * 2 + 1
        // Precompute division table for [0 .. 255*windowSize]
        val dv = IntArray(256 * windowSize) { it / windowSize }

        // --- HORIZONTAL PASS ---
        for (y in 0 until h) {
            var sumR = 0
            var sumG = 0
            var sumB = 0
            val yw = y * w

            // initial window sum
            for (i in -radius..radius) {
                val idx = yw + i.coerceIn(0, w - 1)
                val p = pix[idx]
                sumR += (p shr 16) and 0xFF
                sumG += (p shr 8) and 0xFF
                sumB += p and 0xFF
            }

            // slide window
            for (x in 0 until w) {
                val idx = yw + x
                r[idx] = dv[sumR]
                g[idx] = dv[sumG]
                b[idx] = dv[sumB]

                val subIdx = yw + (x - radius).coerceIn(0, w - 1)
                val addIdx = yw + (x + radius + 1).coerceIn(0, w - 1)
                val ps = pix[subIdx]
                val pa = pix[addIdx]
                sumR += ((pa shr 16) and 0xFF) - ((ps shr 16) and 0xFF)
                sumG += ((pa shr 8) and 0xFF) - ((ps shr 8) and 0xFF)
                sumB += (pa and 0xFF) - (ps and 0xFF)
            }
        }

        // --- VERTICAL PASS ---
        for (x in 0 until w) {
            var sumR = 0
            var sumG = 0
            var sumB = 0

            // initial window sum
            for (i in -radius..radius) {
                val yi = (i.coerceIn(0, h - 1) * w) + x
                sumR += r[yi]
                sumG += g[yi]
                sumB += b[yi]
            }

            // slide window
            for (y in 0 until h) {
                val idx = y * w + x
                resultPix[idx] = (
                        (0xFF shl 24) or
                                (dv[sumR] shl 16) or
                                (dv[sumG] shl 8) or
                                dv[sumB]
                        )

                val subY = ((y - radius).coerceIn(0, h - 1) * w) + x
                val addY = ((y + radius + 1).coerceIn(0, h - 1) * w) + x
                sumR += r[addY] - r[subY]
                sumG += g[addY] - g[subY]
                sumB += b[addY] - b[subY]
            }
        }

        // Build the blurred image
        val blurred = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        blurred.setRGB(0, 0, w, h, resultPix, 0, w)

        // Darken by 50%
        val darkener = RescaleOp(
            floatArrayOf(0.3f, 0.3f, 0.3f, 1f),
            floatArrayOf(0f, 0f, 0f, 0f),
            null
        )
        val result = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        darkener.filter(blurred, result)

        return result
    }

    fun getBackgroundTransform(): AffineTransform {
        val bw = backgroundClear.width.toDouble()
        val bh = backgroundClear.height.toDouble()
        val ww = Window.width.toDouble()
        val wh = Window.height.toDouble()

        // compute scale factor to cover the window
        val scale = if (ww / wh > bw / bh) {
            ww / bw
        } else {
            wh / bh
        }

        // compute translation to center the image
        val tx = (ww - bw * scale) / 2.0
        val ty = (wh - bh * scale) / 2.0

        return AffineTransform().apply {
            translate(tx, ty)
            scale(scale, scale)
        }
    }

    fun drawBackground(g: Graphics2D, isBlur: Boolean) {
        if (!isFastRender) {
            g.drawImage(if (isBlur) scaledBackgroundBlur else scaledBackgroundClear, 0, 0, null)
            return
        }

        val oldHint = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION)
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

        g.drawImage(if (isBlur) backgroundBlur else backgroundClear, getBackgroundTransform(), null)

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldHint)
    }
}
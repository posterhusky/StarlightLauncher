package net.vanolex.composites

import java.awt.Composite
import java.awt.CompositeContext
import java.awt.RenderingHints
import java.awt.image.ColorModel
import java.awt.image.Raster
import java.awt.image.WritableRaster
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


class ShineComposite(val alpha: Float) : Composite {

    override fun createContext(
        srcColorModel: ColorModel?,
        dstColorModel: ColorModel?,
        hints: RenderingHints?
    ): CompositeContext {
        return ShineCompositeContext(alpha, srcColorModel, dstColorModel)
    }

    private class ShineCompositeContext(val alpha: Float, srcCM: ColorModel?, dstCM: ColorModel?) :
        CompositeContext {
        override fun compose(src: Raster, dstIn: Raster, dstOut: WritableRaster) {
            val width = min(src.width.toDouble(), dstIn.width.toDouble()).toInt()
            val height = min(src.height.toDouble(), dstIn.height.toDouble()).toInt()

            val sPix = IntArray(4)
            val dPix = IntArray(4)

            for (y in 0 until height) {
                for (x in 0 until width) {
                    src.getPixel(x, y, sPix)
                    dstIn.getPixel(x, y, dPix)

                    val outPix = IntArray(4)
                    for (i in 0..2) {
                        val Cb = (dPix[i] / 255f)

                        // Soft-light blend per formula
                        val D = if ((Cb <= 0.25f)
                        ) (((16f * Cb - 12f) * Cb + 4f) * Cb)
                        else sqrt(Cb.toDouble()).toFloat()

                        // Composite over using src-over
                        val outC = D * alpha + Cb * (1-alpha)
                        // Clamp and convert back to [0..255]
                        outPix[i] =
                            min(255.0, max(0.0, Math.round(outC * 255f).toDouble())).toInt()
                    }

                    // Set output alpha
                    outPix[3] = dPix[3]
                    dstOut.setPixel(x, y, outPix)
                }
            }
        }

        override fun dispose() {
            // nothing to clean up
        }
    }
}
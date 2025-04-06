package net.vanolex.scenes

import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoMedium
import net.vanolex.fonts.archivoMediumItalic
import net.vanolex.graphics.*
import net.vanolex.graphics.elements.Text
import java.awt.Color
import java.awt.Image

object CompositionBuilder {
    fun buildDialogue(
        title: String, lore: String, hasSpinner: Boolean = false,
        primaryButtonText: String? = null, primaryButtonAction: (() -> Unit)? = null,
        secondaryButtonText: String? = null, secondaryButtonAction: (() -> Unit)? = null,
    ): Composition {
        val titleglyph = archivoBlack.getGlyph(45, title)
        val loreElement = MultilineText(lore, archivoMedium, 540, 18, 230, 180)
        val hasPBtn = primaryButtonText != null || primaryButtonAction != null
        val hasSBtn = secondaryButtonText != null || secondaryButtonAction != null

        val composition = Composition(
            Shade(200, 40, 600, 540, 40),
            Text(titleglyph, (1000-titleglyph.width)/2, 90, Color.WHITE),
            loreElement,
        )

        if (hasSpinner) {
            composition.addElements(
                LoadingSpinner(500,
                (
                        loreElement.y+loreElement.height +
                                if (hasPBtn && hasSBtn) 415
                                else 480
                        )/2 - 25
            )
            )
        }

        if (hasPBtn) {
            composition.addElements(
                SolidButton(220, if (hasSBtn) 415 else 480, 560, 70,
                primaryButtonText ?: "", isPrimary = true, isDisabled = false, primaryButtonAction ?: {})
            )
        }

        if (hasSBtn) {
            composition.addElements(
                SolidButton(220, if (hasPBtn) 500 else 480, 560, if (hasPBtn) 50 else 70,
                secondaryButtonText ?: "", isPrimary = false, isDisabled = false, secondaryButtonAction ?: {})
            )
        }

        return composition
    }

    fun stepDialogue(
        title: String, lore: () -> String, image: () -> Image? = { null },
        maxPages: Int, scene: SelectFortniteDialogue
    ): Composition {
        val titleglyph = archivoBlack.getGlyph(45, title)
        val pageGlyph = { archivoMediumItalic.getGlyph(20, "${scene.page}/$maxPages") }
        val loreElement = DynamicMultilineText(lore, archivoMedium, 540, 18, 230, 180)
        val compostition = Composition(
            Shade(200, 40, 600, 530, 40),
            Text(titleglyph, (1000-titleglyph.width)/2, 90, Color.WHITE),
            loreElement,
            SolidButton(220, 480, 560, 70, "SELECT FILE", isPrimary = true) { scene.openExplorer() },
            SolidButton(220, 415, 50, 50, { "<" }, isPrimary = false, isDisabled = { scene.page <= 1 }) {scene.page -= 1},
            SolidButton(730, 415, 50, 50, { ">" }, isPrimary = false, isDisabled = { scene.page >= maxPages }) {scene.page += 1},
            DynamicText(pageGlyph, { (1000 - pageGlyph().width) / 2 }, { 430 }, Color.WHITE),
        )

        CustomDraw(image) {
            val img = image() ?: return@CustomDraw
            it.drawImage(img, 500-img.getWidth(null)/2, (loreElement.height + 595 + img.getHeight(null))/2, null)
        }

        return compostition
    }

}
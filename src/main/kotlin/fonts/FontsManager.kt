package net.vanolex.fonts

import net.vanolex.config

val paragraphFont = FontImplementation("roboto", 0.75f)
val paragraphFontItalic = FontImplementation("roboto-italic", 0.75f)

val archivoBlack = FontImplementation("archivo-black", 0.75f)
val archivoBlackItalic = FontImplementation("archivo-black-italic", 0.75f)
val interBlack = FontImplementation("inter-black", 0.75f)
val interBlackItalic = FontImplementation("inter-black-italic", 0.75f)
val titleFont get() = if (useAltTitleFont) interBlack else archivoBlack
val titleFontItalic get() = if (useAltTitleFont) interBlackItalic else archivoBlackItalic

val introFont = FontImplementation("intro", 0.75f)
val nougatFont = FontImplementation("nougat", 0.75f)

val useAltTitleFont: Boolean
    get() = try {
        config.langCode in mutableListOf("ru")
    } catch (e: Exception) {
        true
    }

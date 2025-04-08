package net.vanolex.listeners.mouse

import net.vanolex.Panel
import java.awt.MouseInfo

class NormalMouseAction : MouseAction() {
    var clickEligible = true

    override fun tick() {
        if (clickEligible) {
            if (globalClickStart.distanceSq(MouseInfo.getPointerInfo().location) > 400) clickEligible = false
        }
    }

    override fun onRelease() {
        if (!clickEligible) return
        Panel.scene.composition.onClick()
    }
}
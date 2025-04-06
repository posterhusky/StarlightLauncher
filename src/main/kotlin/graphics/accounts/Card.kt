package net.vanolex.graphics.accounts

interface Card {
    val parent: CardScroller
    val isSelected: Boolean
    val selText: String
    val buttonText: String
    fun select()
    fun execAction()
}
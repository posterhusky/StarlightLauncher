package net.vanolex

import kotlinx.serialization.Serializable

@Serializable
data class Lang(
    val selectFile: String, // button text that opens a file selection dialogue
    val launching: String, // title when the game is launching
    val unexpectedError: String, // title
    val unexpectedErrorLore: String, // paragraph explaining that an unexpected error occured
    val next: String, // continue button named next to not conflict with keywords
    val cancel: String, // button text, cancels process or closed current dialogue
    val launched: String, // title, tells the user that the game launch was a succes
    val launchedLore: String, // paragraph explaining that the game was launched
    val mainMenu: String, // button text, returns to the main menu
    val exit: String, // button text, button closes the window
    val linked: String, // title greeting the user after linking the account, `%s` is replaced with the username
    val linkedLore: String, // paragraph explaining that the account was linked
    val save: String, // button text, saves the login credentials to a file
    val launch: String, // found on buttons that launch the game
    val linkAccountShort: String, // used on the main menu card
    val linkAccountAction: String, // used as the action to link the account
    val linkAccount: String, // title of the account linking dialogue
    val linkAccountLore: String, // paragraph of the account linking dialogue
    val waitingLogin: String,
    val waitingLoginLore: String,
    val saving: String,
    val savingLore: String,
    val saved: String,
    val savedLore: String,
    val refresh: String,
    val loadingAccounts: String,
    val selectAccount: String,
    val updateFound: String,
    val updateFoundLore: String,
    val update: String,
    val skip: String,
    val updating: String,
    val updatingLore: String,
    val error: String,
    val selectFortniteLocation: String,
    val exeFileFilter: String,
    val fortniteNotFound: String,
    val findFortnite1: String,
    val findFortnite2: String,
    val findFortnite3: String,
    val findFortnite4: String,
    val findFortnite5: String,
    val fileDoesntExist: String,
    val wrongFile: String,
    val wrongPath: String,
)
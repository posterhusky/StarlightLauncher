package net.vanolex.tasks

class EpicAPIException(responseBody: String): Exception("API response contains an unexpected error:\n$responseBody")
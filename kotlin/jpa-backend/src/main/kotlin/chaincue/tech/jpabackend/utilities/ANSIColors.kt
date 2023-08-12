package chaincue.tech.jpabackend.utilities


interface ANSIColors {
    companion object {
        const val ANSI_RESET = "\u001B[0m"
        const val ANSI_BLACK = "\u001B[30m"
        const val ANSI_RED = "\u001B[31m"
        const val ANSI_GREEN = "\u001B[32m"
        const val ANSI_YELLOW = "\u001B[33m"
        const val ANSI_BLUE = "\u001B[34m"
        const val ANSI_PURPLE = "\u001B[35m"
        const val ANSI_CYAN = "\u001B[36m"
        const val ANSI_WHITE = "\u001B[37m"
        fun printBlue(text: Any) = println("$ANSI_BLUE$text$ANSI_RESET")
        fun printPurple(text: Any) = println("$ANSI_PURPLE$text$ANSI_RESET")
        fun printGreen(text: Any) = println("$ANSI_GREEN$text$ANSI_RESET")
    }
}


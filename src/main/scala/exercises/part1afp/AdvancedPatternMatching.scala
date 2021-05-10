package exercises.part1afp

object AdvancedPatternMatching{
    object IntMatching {
        def unapply(item: Int): Option[(String, Int)] = item match {
            case x if x < 10 => Some(("single digit", x))
            case x if x % 2 == 0 => Some(("an even", x))
            case x if x % 2 != 0 => Some(("an odd", x))
            case _ => None
        }
    }

    object even {
        def unapply(item: Int): Option[Boolean] =
            if (item % 2 == 0) Some(true)
            else None
    }

    // we can can also do it this way:
    object singleDigit {
        def unapply(item: Int): Boolean = item < 10
    }
}

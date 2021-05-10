package playground

import exercises.part1afp.AdvancedPatternMatching._

object Hello extends App {
    val digits = List(3, 12, 15)
    digits.foreach {
        case IntMatching(message) => println(message)
    }

    digits.foreach {
        case singleDigit() => println("single digit")
        case even(_) => println("even")
        case _ => println("odd")
    }

}

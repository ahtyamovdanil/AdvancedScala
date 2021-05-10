package lectures.part2.afp

object PartialFunctions extends App {
    val aFunction = (x: Int) => x + 1

    val aNicerFussyFunction = (x: Int) => x match {
        case 1 => 42
        case 2 => 56
        case 5 => 999
    }
    // {1,2,5} => Int

    val aPartialFunction: PartialFunction[Int, String] = {
        case 1 => "kek"
        case 2 => "chebureck"
        case 5 => "mama mia"
    } // partial function value

    println(aPartialFunction(2))

    // PF utilities
    println(aPartialFunction.isDefinedAt(1))

    // lift
    val lifted = aPartialFunction.lift // Int => Option[String]
    println(lifted(2))
    println(lifted(98))

    val pfChain = aPartialFunction.orElse[Int, String] {
        case 98 => "ninety eight"
    }

    println(pfChain(2))
    println(pfChain(98))

    // PF extend normal functions
    val totalFunction: Int => String = {
        case 1 => "one"
    }

    // high order functions accept partial functions as well
    val aMappedList = List(1,2,3).map {
        case 1 => 42
        case 2 => 78
        case 3 => 1000
    }
    println(aMappedList)

    /*
     Partial functions can only have ONE parameter type
     */
}

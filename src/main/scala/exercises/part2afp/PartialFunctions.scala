package exercises.part2afp

object PartialFunctions extends App {

    // his implementation
    val aManualPartialFunction = new PartialFunction[Int, Int] {
        override def isDefinedAt(x: Int): Boolean =
            x == 1 || x == 2 || x == 3

        override def apply(v1: Int): Int = v1 match {
            case 1 => 42
            case 2 => 56
            case 3 => 123
        }
    }

    // my implementation
    object MyPartialFunctionImpl extends (String => String) {
        override def apply(value: String): String = value match {
            case "Hello" => "Привет"
            case "Привет" => "Hello"
        }

        def isDefinedAt(x: String): Boolean =
            x.matches("^[a-zA-Z]*$") // contain only letters
    }

    //val mpf = new MyPartialFunctionImpl
    //scala.io.Source.stdin.getLines().foreach(item => println(MyPartialFunctionImpl(item)))
    scala.io.Source.stdin.getLines().map(MyPartialFunctionImpl).foreach(println)
}

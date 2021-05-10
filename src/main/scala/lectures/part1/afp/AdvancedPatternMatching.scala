package lectures.part1.afp

object AdvancedPatternMatching extends App {

    class Person(val name: String, val age: Int)

    object Person {
        def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))
    }


    val bob = new Person("bob", 25)
    val greeting = bob match {
        case Person(name, age) => s"hello, my name is $name and I'm $age yo!"
    }
    println(greeting)


    class MyPerson(val name: String, val age: Int)


   // the object can also can hase different name
    object MyPersonPattern {
        def unapply(person: MyPerson): Option[(String, Int)] =
            if (person.age < 21) None  // idk if this can be useful
            else Some((person.name, person.age))
    }

    val daniel = new MyPerson("Obama", 21)
    val greeting2 = daniel match {
        case MyPersonPattern(name, age) => s"hello, my name is $name and I'm $age yo!"
        case _ => "Sorry, you are not welcome here("
    }
    println(greeting2)

    // you can define multiple unapply methods
    object MyComplexPersonPattern {
        def unapply(person: MyPerson): Option[(String, Int)] = Some((person.name, person.age))
        def unapply(age: Int): Option[String] =
            Some(if (age < 21) "minor" else "major")
        def unapply(name: String): Option[String] =
            Some(if (name == "Obama") "the president" else "a pleb")
    }

    val legalStatus = daniel.age match {
        case MyComplexPersonPattern(status) => s"My legal status is $status"
    }

    val presidentStatus = daniel.name match {
        case MyComplexPersonPattern(status) => s"I'm $status"
    }

    println(s"Hello $legalStatus and $presidentStatus")



}

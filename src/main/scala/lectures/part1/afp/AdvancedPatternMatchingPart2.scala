package lectures.part1.afp

import AdvancedPatternMatching.{Person, bob}

object AdvancedPatternMatchingPart2 extends App {

    // infix patterns
    case class Or[A, B](a: A, b: B) //Either
    val either = Or(2, "two")
    val humanDescription = either match {
        // only works with two things in a pattern
        case number Or string => s"$number is written as $string"
    }

    // decomposing sequences
    val numbers = List(1,2,3)
    val vararg = numbers match {
        case List(1, _*) => "starts with 1"
    }

    abstract class MyList[+A] {
        def head: A = ???
        def tail: MyList[A] = ???
    }

    case object MyEmptyList extends MyList[Nothing]
    case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

    object MyList {
        def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = list match {
            case MyEmptyList => Some(Seq.empty)
            case _ => unapplySeq(list.tail).map(list.head +: _)
        }
    }

    val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Cons(4, MyEmptyList))))
    val decomposed = myList match {
        case MyList(1, 2, _*) => "starting with 1, 2"
        case _ => "something else"
    }

    // custom return type for unapply
    // isEmpty: Boolean ; get: something
    abstract class Wrapper[T]{
        def isEmpty: Boolean
        def get: T
    }

    object PersonWrapper {
        def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
            def isEmpty = false
            def get: String = person.name
        }
    }

    val bob = new Person("Bob", 21)
    println(bob match {
        case PersonWrapper(n) => s"this person name is $n"
        case _ => "an alien"
    })
}


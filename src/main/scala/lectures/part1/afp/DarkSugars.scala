package lectures.part1.afp

import scala.util.Try

object DarkSugars extends App {

    // syntax sugar #1:
    //methods with single parameters
    def singleArgMethod(arg: Int): String = s"$arg little ducks..."

    // you can use curly bracers to call function with one argument
    val description = singleArgMethod {
        // write sum code and return some result
        42
    }

    // this feature is used in Try function for example
    val aTryInstance = Try {
        throw new RuntimeException
    }

    // and in map function too
    List(1,2,3).map {
        x => x+1
    }

    // syntax sugar #2:
    // single abstract method
    trait Action {
        def act(item: Int): Int
    }

    val anInstance: Action = new Action {
        override def act(item: Int): Int = item + 1
    }
    // ^ v this two pieces are equal
    val aFunkyInstance: Action = (item: Int) => item + 1

    // examples:
    val aThread = new Thread(new Runnable {
        override def run(): Unit = println("hello world!")
    })

    val aFunkyThread = new Thread(() => println("hello world"))

    // another example
    abstract class MyClass {
        def myImplemented = 42
        def foo(x: Int): Unit
    }

    val aFunkyMyClass = (x: Int) => println(x + 1)

    // syntax sugar #3:
    // the `::` and `#::` methods are special
    val prependedList = 1 :: 2 :: 3 :: List(4, 5 ,6)

    //this will be rewritten by scala to:
    List(4, 5, 6).::(3).::(2).::(1)

    //you can write one prepend methods with using of `:` in the end of the method name
    class MyStream[T] {
        def -->:(value: T): MyStream[T] = this //actual implementation is here
    }

    val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

    // syntax sugar 4:
    // multi-word method naming
    class MyMultiWordMethodClass {
        def `and then message`(message: String): Unit = println(message)
    }

    val oleg = new MyMultiWordMethodClass()
    oleg `and then message` "hello"

    // syntax sugar 5:
    // infix types
    class Composite[A, B]
    val composite: Int Composite String = ???

    class -->[A, B]
    val towards: Int --> String = ???

    // syntax sugar 6:
    // `update` method is very special, much like apply
    val anArray = Array(1,2,3)
    anArray(2) = 7 //anArray.update(2, 7) // {1,2,3} -> {1,2,7}
    // used in mutable collections

    // syntax sugar 7:
    // setters for mutable collections
    class Mutable{
        private var internalMember: Int = 0
        def member: Int = internalMember    //getter
        def member_=(value: Int): Unit = {  //setter
            internalMember = value
        }
    }

    val myMutableContainer = new Mutable
    myMutableContainer.member = 42      // rewritten as myMutableContainer.member_=(42)
}

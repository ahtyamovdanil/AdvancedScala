package exercises.part2afp

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
    def apply(value: A): Boolean = contains(value)

    def contains(value: A): Boolean

    def +(value: A): MySet[A]

    def ++(other: MySet[A]): MySet[A]

    def map[B](f: A => B): MySet[B]

    def flatMap[B](f: A => MySet[B]): MySet[B]

    def filter(predicate: A => Boolean): MySet[A]

    def foreach(f: A => Unit): Unit

    def -(value: A): MySet[A]

    def &(other: MySet[A]): MySet[A] // intersection

    def --(other: MySet[A]): MySet[A]

    def <->(other: MySet[A]): MySet[A]

    def unary_! : MySet[A]
}

// decsribes all elements of type A which satisfy a property
// {x in A | property(x)}
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
    override def contains(value: A): Boolean = property(value)

    override def +(value: A): MySet[A] =
        new PropertyBasedSet[A](x => property(x) || (x == value))

    override def ++(other: MySet[A]): MySet[A] =
        new PropertyBasedSet[A](x => property(x) || other(x))

    override def map[B](f: A => B): MySet[B] = politelyFail

    override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

    override def filter(predicate: A => Boolean): MySet[A] =
        new PropertyBasedSet[A](x => property(x) && predicate(x))

    override def foreach(f: A => Unit): Unit = politelyFail

    override def -(value: A): MySet[A] =
        new PropertyBasedSet[A](filter(x => x != value))

    override def &(other: MySet[A]): MySet[A] =
        new PropertyBasedSet[A](filter(other))

    override def --(other: MySet[A]): MySet[A] =
        new PropertyBasedSet[A](filter(!other))

    override def <->(other: MySet[A]): MySet[A] = {
        // !inner & (this || other)
        new PropertyBasedSet[A](!filter(other) & (this ++ other))
    }

    override def unary_! : MySet[A] =
        new PropertyBasedSet[A](x => !property(x))

    def politelyFail = throw new IllegalArgumentException("we don`t do that here")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

    override def +(value: A): MySet[A] =
        if (this contains value) this
        else new NonEmptySet(value, this)

    override def contains(value: A): Boolean =
        (head == value) || tail.contains(value)

    override def ++(other: MySet[A]): MySet[A] = {
        tail ++ other + head
    }

    override def map[B](f: A => B): MySet[B] =
        tail.map(f) + f(head)

    override def flatMap[B](f: A => MySet[B]): MySet[B] =
        tail.flatMap(f) ++ f(head)

    override def filter(predicate: A => Boolean): MySet[A] = {
        if (predicate(head))
            tail.filter(predicate) + head
        else
            tail.filter(predicate)
    }

    override def foreach(f: A => Unit): Unit = {
        f(head)
        tail.foreach(f)
    }

    override def apply(value: A): Boolean = contains(value)

    // exclude value from set
    override def -(value: A): MySet[A] = {
        if (head == value) tail
        else tail - value + head
    } //set - value

    // intersection
    override def &(other: MySet[A]): MySet[A] = {
        filter(other) // intersection = filtering
    }

    // set1 - set2
    override def --(other: MySet[A]): MySet[A] =
        filter(!other)

    override def <->(other: MySet[A]): MySet[A] = {
        val inner = &(other)
        (this -- inner) ++ (other -- inner)
    } // outer

    override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !contains(x))
}

class EmptySet[A]() extends MySet[A] {
    override def contains(value: A): Boolean = false

    override def +(value: A): MySet[A] = new NonEmptySet(value, new EmptySet[A])

    override def ++(other: MySet[A]): MySet[A] = other

    override def map[B](f: A => B): MySet[B] = new EmptySet[B]

    override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

    override def filter(predicate: A => Boolean): MySet[A] = this

    override def foreach(f: A => Unit): Unit = ()

    override def -(value: A): MySet[A] = this

    override def &(other: MySet[A]): MySet[A] = this

    override def --(other: MySet[A]): MySet[A] = this

    override def <->(other: MySet[A]): MySet[A] = other

    override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

object MySet {
    def apply[A](values: A*): MySet[A] = {
        @tailrec
        def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
            if (valSeq.isEmpty) acc
            else buildSet(valSeq.tail, acc + valSeq.head)
        }
        buildSet(values, new EmptySet[A])
    }

    // you can also use foldLeft method
    def myApply[A](values: A*): MySet[A] = {
        values.foldLeft[MySet[A]](new EmptySet[A])(_ + _)
    }
}

object mySetPlayground extends App {

    def myPrint(item: Any): Unit = print(s"$item ")

    val mySet = MySet(1, 2, 3, 4)
    val mySet2 = MySet(1, 2, 3, 10, 9, 8)

    (mySet + 1).foreach(myPrint)
    println()

    mySet.filter(_ % 2 == 0).foreach(myPrint)

    println("\nmap:")
    mySet.map(_ * 10).foreach(myPrint)

    println("\nflatMap:")
    mySet.flatMap(item => MySet(item, 10 * item)).foreach(myPrint)

    println("\nintersection:")
    (mySet & mySet2).foreach(myPrint)

    println("\ndifference:")
    (mySet -- mySet2).foreach(myPrint)

    println("\nsymmetricDifference:")
    (mySet <-> mySet2).foreach(myPrint)
    println()

    val negative = !mySet
    println(negative(2))
    println(negative(10))

    val negativeEven = negative.filter(_ % 2 == 0)
    println(negativeEven(13))

    val negativeEvenFive = negativeEven + 5 // all the even numbers > 4 and 5
    println(negativeEvenFive(5))
}



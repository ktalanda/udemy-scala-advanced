package lectures.part1as

import scala.util.Try

object DarkSugars extends App {

  // syntax sugar #1: methods with single param
  def singleArgumentMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgumentMethod {
    // write some complex code
    42
  }

  val aTryInstance = Try {
    throw new RuntimeException
  }

  List(1, 2, 3).map { x => x + 1 }

  // syntax sugar #2: single abstract method
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }
  val aFuncInstance: Action = (x: Int) => x + 1

  // example: Runnable
  val aThread = new Runnable {
    override def run(): Unit = println("Hello Scala")
  }
  val aSweeterThread = new Thread(() => println("sweet, Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23

    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  //syntax sygar #3: the :: and #:: methods are special
  val prependList = 2 :: List(3, 4)

  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax sugar #4: multi word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }
  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is sweet"

  // syntax sugar #5: infix types
  class Composite[A, B]
  val composite: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  // syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewritten to anArray.update(2, 7)
  // used in mutable collections

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member: Int = internalMember // "getter"
    def member_=(value: Int): Unit = {
      internalMember = value // "setter"
    }
  }
  val aMutableContainer = new Mutable
  aMutableContainer.member = 42
}

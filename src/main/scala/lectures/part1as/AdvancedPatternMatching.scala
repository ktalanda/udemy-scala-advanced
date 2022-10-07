package lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
  */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  println(legalStatus)

  val n: Int = 45

  object IntPattern {
    def unapply(value: Int): Option[String] = Some(
      if (value < 10) "single digit"
      else if (value % 2 == 0) "an even number"
      else "no property"
    )
  }

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  }
  val mathPropertyWithPattern = n match {
    case IntPattern(value) => value
  }
  val mathPropertyWithBetterPattern = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  println(mathPropertyWithBetterPattern)
}

package lectures.part4implicits

object OrganisingImplicits extends App {
  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  //  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)

  println(List(1, 4, 5, 3, 2).sorted)

  /*
  Implicits (used as implicit parameters):
   - val/var
   - object
   - accessor methods = defs with no parentheses
  */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  //  implicit def personOrdering: Ordering[Person] = Ordering.fromLessThan(_.age < _.age)

  //  println(persons.sorted)

  /*
  Implicit scope
  - normal scope = LOCAL SCOPE
  - imported scope
  - companion objects of all types involved in the method signature
  */

  object AlphabeticNameOrdering {
    implicit val alphabeticNameOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan(_.age < _.age)
  }

  import AgeOrdering._

  println(persons.sorted)

  case class Purchase(nUnits: Int, unitPrice: Int) {
    val totalPrice = unitPrice * nUnits
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.totalPrice < _.totalPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  import UnitPriceOrdering._
  val purchases = List(
    Purchase(1, 200),
    Purchase(10, 100)
  )
  println(purchases.sorted)
}

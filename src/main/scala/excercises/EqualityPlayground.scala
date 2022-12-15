package excercises

import lectures.part4implicits.TypeClasses._

object EqualityPlayground extends App {

  trait Equal[T] {
    def apply(left: T, right: T): Boolean
  }

  object NameEqual extends Equal[User] {
    override def apply(left: User, right: User): Boolean = left.name == right.name
  }

  implicit object AgeEquality extends Equal[User] {
    override def apply(left: User, right: User): Boolean = left.age == right.age
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val john = User("John", 32, "john@sample.com")
  val anotherJohn = User("John", 12, "test@test.com")
  println(Equal.apply(john, anotherJohn))

  //AD-HOC polymorphism

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = !equalizer.apply(value, other)

    println(john === anotherJohn)
  }
}

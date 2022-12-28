package lectures.part5ts

object TypeMemebers extends App {

  sealed class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

//  val cat: ac.BoundedAnimal = new Cat
  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value:Int) extends MyList {
    override type T = Int
    override def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat

  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

//  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
//    type A = String
//    def head = hd
//    def tail = tl
//  }

  class IntList(hd: Integer, tl: IntList) extends MList with ApplicableToNumbers {
    type A = Integer
    def head = hd
    def tail = tl
  }
}

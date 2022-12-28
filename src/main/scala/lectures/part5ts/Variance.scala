package lectures.part5ts

object Variance extends App {
  trait Animal

  class Dog extends Animal

  class Cat extends Animal

  class Crocodile extends Animal

  class Cage[T]

  // convariance
  class CCage[+T]

  val ccage: CCage[Animal] = new CCage[Cat]

  // invariance
  class ICage[T]
  //  val iCage: ICage[Animal] = new ICage[Cat]

  // contravariance
  class XCage[-T]

  val xCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T)

  class CovariantCate[+T](val animal: T)
  // class ContravariantCage[-T](val animal: T)
  // class CovariantVariableCage[+T](var animal: T)

  //  trait AnotherCovariantCage[+T] {
  //    def addAnimal(animal: T): Unit
  //  }

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }
  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = animals.add(new Dog)

  class PetShop[-T] {
//    def get(isItPuppy: Boolean): T

  def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  sealed class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  class IParking[T](things: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicle(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[T] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicle(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicle[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???
  }

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[T] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicle[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicle[S <: T](conditions: String): IList[S] = ???
  }
}

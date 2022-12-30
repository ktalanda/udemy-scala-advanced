package lectures.part5ts

object PathDependentTypes extends App {
  class Outer {
    class Inner

    object InnerObject

    type InnerType

    def print(i: Inner) = println(i)

    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    42
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner

  val oo = new Outer
  //  val otherInner: oo.Inner = new o.Inner
  o.print(inner)
  //oo.print(inner)
  o.printGeneral(inner)
  oo.printGeneral(inner)

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

//  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???
//
//  get[IntItem](42)
//  get[StringItem]("home")

}

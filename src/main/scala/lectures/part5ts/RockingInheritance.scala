package lectures.part5ts

object RockingInheritance extends App {

  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[T] {
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem
  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "lion" }
  trait Tiger extends Animal { override def name: String = "tiger" }
  class Mutant extends Lion with Tiger
  val m = new Mutant
  println(m.name)
  // LAST OVERRIDE GETS PICKED

  // the super problem + type linearization
  trait Cold {
    def print = println("cold")
  }
  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }
  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }
  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print
}

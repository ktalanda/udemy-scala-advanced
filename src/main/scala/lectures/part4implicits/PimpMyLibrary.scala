package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timeAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timeAux(n - 1)
        }


      timeAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n-1) ++ list

      concatenate(value)
    }
  }

  new RichInt(42).sqrt
  42.isEven
  // type enrichment = pimping

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  implicit class RichString(value: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(value)

    def encrypt(cypherDistance: Int): String = value.map(c => (c+cypherDistance).asInstanceOf[Char])

  }

  println("1".asInt)
  println("John".encrypt(2))

  3.times(() => println("Scala"))
  println(4 * List(1,2))

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6"/2)

  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)
}

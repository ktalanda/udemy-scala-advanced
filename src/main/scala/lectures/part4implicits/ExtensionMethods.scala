package lectures.part4implicits

class ExtensionMethods extends App {
  case class Person(name: String) {
    def greet(): String = s"Hi, I'm $name how can I help?"
  }

  extension (string: String) {
    def greetAsPerson(): String = Person(string).greet()
  }

  val danielsGreeting = "Daniel".greetAsPerson()

  object Scala2ExtensionMethods {
//    implicit class RichInt(value: Int) extends AnyVal {
//      def isEven: Boolean = value % 2 == 0
//
//      def sqrt: Double = Math.sqrt(value)
//
//      def times(function: () => Unit): Unit = {
//        def timeAux(n: Int): Unit =
//          if (n <= 0) ()
//          else {
//            function()
//            timeAux(n - 1)
//          }
//
//
//        timeAux(value)
//      }
//    }
  }

  val is3Even = 3.isEven

  extension (value: Int) {
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
  }

  extension [A](list: List[A]) {
    def ends: (A, A) = (list.head, list.last)
    def extremes(using ordering: Ordering[A]): (A, A) = list.sorted.ends
  }
}

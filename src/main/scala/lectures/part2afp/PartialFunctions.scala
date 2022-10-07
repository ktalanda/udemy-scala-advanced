package lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] == Int => Int
  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFuction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2))
//  println(aPartialFunction(22))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted: Int => Option[Int] = aPartialFunction.lift
  println(lifted(2))
  println(lifted(22))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOF accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  // pf can only have ONE parameter type

  val myPartialFunction = new PartialFunction[String, Int]{
    override def isDefinedAt(x: String): Boolean =
      x == "one" || x == "two"

    override def apply(v1: String): Int = {
      v1 match {
        case "one" => 1
        case "two" => 2
      }
    }
  }
  scala.io.Source.stdin.getLines().foreach(line =>
    println(myPartialFunction.lift(line))
  )

  val chatbot: PartialFunction[String, String] = {
    case "what" => "nothing"
  }
  scala.io.Source.stdin.getLines().foreach(line => println(chatbot.lift(line)))
}

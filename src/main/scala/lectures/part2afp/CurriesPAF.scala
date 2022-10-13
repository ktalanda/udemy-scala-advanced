package lectures.part2afp

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int = x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y

  println(add3(5))
  println(superAdder(3)(5))

  // method
  def curriedAdder(x: Int)(y: Int): Int = x + y

  // function value
  val add4: Int => Int = curriedAdder(4)

  // lifting = ETA-EXPANSION - transforming methods to functions

  def inc(x: Int) = x + 1

  List(1, 2, 3).map(inc) // ETA-expansion - List(1,2,3).map(x => inc(x))

  // Partial funciton applications
  val add5 = curriedAdder(5) _ // Int => Int

  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curriedAddMethod(x: Int)(y: Int) = x + y

  val add7_0 = (x: Int) => simpleAddMethod(x, 7)
  val add7_1 = (x: Int) => simpleAddFunction(x, 7)
  val add7_2 = curriedAddMethod(7) _
  val add7_2_1 = curriedAddMethod(7)(_)

  def add7_3(x: Int) = simpleAddMethod(x, 7)

  val add7_4 = simpleAddMethod.curried(7)
  val add7_5 = simpleAddMethod(7, _: Int)

  // underscores are powerful
  def concatinator(a: String, b: String, c: String) = a + b + c
  val insertName = concatinator("Hello, I'm ", _: String, ", how are you") // x: String => concatinator("Hello, I'm", x, ", how are you")
  println(insertName("Kamil"))

  val fillInTheBlanks = concatinator("Hello, ", _: String, _: String) // (x, y) => concatinator("Hello, ", x, y)
  println(fillInTheBlanks("Kamil", " Scala is awesome!"))

  println("%4.2f".format(Math.PI))
  println("%8.6f".format(Math.PI))

  val formatter: String => Double => String = format => x => format.format(x)

  println(formatter("%4.2f")(Math.PI))

  val format_4_2 = formatter("%4.2f")
  val format_8_6 = formatter("%8.6f")
  val format_14_12 = formatter("%14.12f")

  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  byName(42)
  byName(method)
  byName(parenMethod())

  byFunction(() => 42)
  byFunction(() => method)
  byFunction(parenMethod)
}

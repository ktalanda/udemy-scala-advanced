package lectures.part2afp

object Monads extends App {

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
    left-dentity

    unit.flatMap(f) = f(x)
    Attempt(x).flatMap(f) = f(x)
    Success(x).flatMap(f) = f(x)

    right-identity
    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    Fail(_).flatMap(...) = Fail(e)

    associativity
    attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)
    Success(v).flatMap(f).flatMap(g) = f(v).flatMap(g) OR Fail(e)
    Success(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMpa(g) OR Fail(e)
  */

  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]) = f(internalValue)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  flatMappedInstance.use
  flatMappedInstance2.use

  /*
  left-identity
  unit.flatMap(f) = f(v)
  Lazy(v).flatMap(f) = f(v)

  right-identity
  l.flatMap(unit) = l
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  associativity
  l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)

  */

  /*

  Monad[T] {
    def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)

    def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
    def flatted(m: Monad[Monad[T]]): Moand[T] = m.flatMap(x: Monad[T] => x)
  }

  */
}


package excercises

trait MySet[A] extends (A => Boolean) {
  override def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  def -(elem: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A] // intersection
  def --(anotherSet: MySet[A]): MySet[A] // difference

  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {
  def contains(elem: A) = false
  def +(elem: A): MySet[A] = NonEmptySet(elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = EmptySet[B]()
  def flatMap[B](f: A => MySet[B]): MySet[B] = EmptySet[B]()
  def filter(predicate: A => Boolean): MySet[A] = EmptySet[A]()
  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = PropertyBasedSet[A](_ => true)
}

// all elements of type A which satisfy the property
// { x in A | property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)
  def +(elem: A): MySet[A] = PropertyBasedSet[A](x => property(x) || x == elem)
  def ++(anotherSet: MySet[A]): MySet[A] = PropertyBasedSet[A](x => property(x) || anotherSet.contains(x))

  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def filter(predicate: A => Boolean): MySet[A] = PropertyBasedSet[A](x => property(x) && predicate(x))
  def foreach(f: A => Unit): Unit = politelyFail

  def -(elem: A): MySet[A] = filter(x => x != elem)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def unary_! : MySet[A] = PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw IllegalArgumentException("Really deep rabbit hole!")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean = elem == head || tail.contains(elem)
  def +(elem: A): MySet[A] =
    if (this contains elem) this
    else NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head
  def map[B](f: A => B): MySet[B] = (tail map f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)
  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }
  def foreach(f: A => Unit): Unit =
    f(head)
    tail foreach f

  def -(elem: A): MySet[A] =
    if (head == elem) tail
    else tail - elem + head
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersection = filtering
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def unary_! : MySet[A] = PropertyBasedSet[A](x => !contains(x))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, 10 * x)) filter (_ % 2 == 0) foreach println

  val negative = !s
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5
  println(negativeEven5(5))

  val test = s -- MySet(1,2)
  println(test(1))
  println(test(3))

  val test1 = s & MySet(1,2)
  println(test1(1))
  println(test1(3))
}
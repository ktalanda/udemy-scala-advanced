package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object FuturesPromises extends App {

  def calculateTheMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateTheMeaningOfLife // on ANOTHER THREAD
  }

  println(aFuture.value)

  println("waiting on future")

  //  aFuture.onComplete(t => t match {
  //    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
  //    case Failure(e) => println(s"I have failed with $e")
  //  }) // SOME thread
  //  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fp.id.1-zuck" -> "Mark",
      "fp.id.2-bill" -> "Bill",
      "fp.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fp.id.1-zuck" -> "fp.id.2-bill"
    )
    val random = new Random

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  val mark = SocialNetwork.fetchProfile("fp.id.1-zuck")
  mark.onComplete {
    case Success(markProfile) =>
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    case Failure(ex) => ex.printStackTrace()
  }

  // functional compsition of futures
  // map, flatMap, filter
  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  for {
    mark <- SocialNetwork.fetchProfile("fp.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(3000)

  // fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown").recover {
    case e: Throwable => Profile("fp.id.0-dummy", "Forever alone")
  }
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fp.id.0-dummy")
  }
  val fallbackResult = SocialNetwork.fetchProfile("unknown").fallbackTo(SocialNetwork.fetchProfile("fp.id.0-dummy"))

  // online banking app
  case class User(name: String)

  case class Transaction(sender: String, receiver: String, amount: Int, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Int): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Int): String = {
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
    }
  }

  println(BankingApp.purchase("Kamil", "iPhone 12", "rock", 300000))

  // promises
  val promise = Promise[Int]
  val future = promise.future

  // thread 1 - "consumer"
  future.onComplete {
    case Success(r) => println(s"[consumer] I've received $r")
  }

  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    promise.success(42)
    println("[producer] done")
  })

  producer.start()

  Thread.sleep(1000)

  // fulfill a future IMMEDIATELY with a value
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // inSequence(fa, fb)
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(a => second)

  // first out of 2
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]
    fa.onComplete(promise.tryComplete)
    fb.onComplete(promise.tryComplete)

    promise.future
  }

  // last out of 2
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete = (result: Try[A]) =>
      if (!bothPromise.tryComplete(result)) lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }
  val slow = Future {
    Thread.sleep(200)
    45
  }

  first(fast, slow).foreach(f => println(s"first $f"))
  last(fast, slow).foreach(l => println(s"last $l"))

  Thread.sleep(1000)


  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
      .filter(condition)
      .recoverWith { case _ => retryUntil(action, condition) }

  val random = Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " + nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x < 50).foreach(result => println("settled at " + result))

  Thread.sleep(10000)
}

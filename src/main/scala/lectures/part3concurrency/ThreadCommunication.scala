package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
    The producer-consumer problem
    producer -> [?] -> consumer
  */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get = {
      var result = value
      value = 0
      result
    }
  }

  def naiveProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while (container.isEmpty) {
        println("[consumer] actively waiting...")
      }
      println(s"[consumer] I have consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] I have produced, after long work, the value $value")
      container.set(value)
    })
    //    consumer.start()
    //    producer.start()
  }

  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }
      println(s"[consumer] I have consumed ${container.get}")
    })
    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(2000)
      val value = 42
      container.synchronized {
        println(s"[producer] I'm producing $value")
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()

  }

  //  naiveProducerConsumer()
  //  smartProducerConsumer()

  /*
    producer -> [ ? ? ? ] -> consumer
  */
  val capacity = 3

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]

    val consumer = new Thread(() => {
      val random = new Random
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer is empty, waiting...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer] consuming $x")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting")
            buffer.wait()
          }
          println(s"[producer] producing $i")
          buffer.enqueue(i)

          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(250))
      }
    })
    consumer.start()
    producer.start()
  }
  //  prodConsLargeBuffer()

  /*
    Prod-cons, level 3

    producer1 -> [ ? ? ? ] -> consumer1
    producer2 -> [ ? ? ? ] -> consumer2
  */

  def createConsumer(buffer: mutable.Queue[Int], name: String): Thread = {
    new Thread(() => {
      val random = new Random
      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer $name] buffer is empty, waiting...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer $name] consuming $x")

          buffer.notifyAll()
        }
        Thread.sleep(random.nextInt(500))
      }
    })
  }

  def createProducer(buffer: mutable.Queue[Int], name: String, capacity: Int): Thread = {
    new Thread(() => {
      val random = new Random
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $name] buffer is full, waiting")
            buffer.wait()
          }
          println(s"[producer $name] producing $i")
          buffer.enqueue(i)

          buffer.notifyAll()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })
  }

  def multiProdConsSharedBuffer(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20


    (1 to nConsumers).foreach { i => createConsumer(buffer, i.toString).start() }
    (1 to nProducers).foreach { i => createProducer(buffer, i.toString, capacity).start() }
  }
  //  multiProdConsSharedBuffer(3,3)

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println(s"[announcer] rock'n roll!")

      bell.synchronized {
        bell.notifyAll()
      }
    }).start()
  }
  //  testNotifyAll()

  // deadlock
  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"${this} I am bowing to my friend ${other}")
        other.rise(this)
        println(s"${this} my friend ${other.name} has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized {
        println(s"${this} I am rising to my friend ${other}")
      }
    }

    var side = "right"

    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while (this.side == other.side) {
        println(s"$this: Oh but please, $other, feel free to pass...")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = Friend("sam")
  val pierre = Friend("pierre")
  //  new Thread(() => sam.bow(pierre)).start()
  //  new Thread(() => pierre.bow(sam)).start()

  new Thread(() => sam.pass(pierre)).start()
  new Thread(() => pierre.pass(sam)).start()

  // livelock

}

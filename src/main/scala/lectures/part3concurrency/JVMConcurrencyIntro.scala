package lectures.part3concurrency

import java.util.concurrent.Executors

object JVMConcurrencyIntro extends App {

  // JVM threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val aThread = new Thread(runnable)

  aThread.start()
  runnable.run()
  aThread.join()

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodBye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodBye.start()

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))
  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
//  pool.execute(() => println("should not appear")) // throws in the calling thread

//  pool.shutdownNow()
  println(pool.isShutdown)
}

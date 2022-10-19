package lectures.part3concurrency

object JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => { x = 1 })
    val thread2 = new Thread(() => { x = 2 })
    thread1.start()
    thread2.start()
    println(x)
  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.synchronized {
      bankAccount.amount -= price // critical section
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 100000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iphone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()

      if (account.amount != 43000) println(s"AHA I've just broken the bank: ${account.amount}")
    }
  }

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"hello from thread $i")
    })

  def startThread(n: Int): Unit = {
    if (n > 0) {
      val thread = new Thread(() => {
        println(s"hello from thread $n")
        startThread(n - 1)
      })
      thread.start()
      thread.join()
    }
  }

  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    Thread.sleep(1000)
    println(x)
  }

  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })
    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(920)
    awesomeThread.join()
    println(message)

  }

  def main(args: Array[String]): Unit = {
    inceptionThreads(50)
  }
}

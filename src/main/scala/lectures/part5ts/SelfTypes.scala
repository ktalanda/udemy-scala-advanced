package lectures.part5ts

object SelfTypes extends App {

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist =>
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  val jamesHetfield = new Singer with Instrumentalist:
    override def play(): Unit = ???
    override def sing(): Unit = ???

  class Guitarist extends Instrumentalist {
    override def play(): Unit = ???
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  class A
  class B extends A // B IS AN A

  trait T
  trait S { self: T => } // S REQUIRES T

  // CAKE PATTERN = > dependency injection
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks"
  }


  // level 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // level 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytic extends ScalaDependentComponent with Stats

  // level 3 - app
//  trait AnalyticsApp extends ScalaApplication with Analytic

//  class X extends Y
//  class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }
}

import Actors.{Client, ReservationService, SystemService}
import Messages.Query
import akka.actor.{ActorSystem, Props}

object SystemStarter extends App {
  val system = ActorSystem("AkkaApplication")
  val ReservationActor = system.actorOf(Props[ReservationService], "ReservationService")
  val SystemActor = system.actorOf(Props(new SystemService(ReservationActor)), "SystemService")
  Thread.sleep(500) // a small delay to allow the SystemActor to set up its cache
  val ClientActor = system.actorOf(Props(new Client(SystemActor, "christophe", 23, "123456", Query("hotel", "25-12-2021", category = Some(5)))), "Client")
  val ClientActor2 = system.actorOf(Props(new Client(SystemActor, "olivier", 25, "234567", Query("hotel", "25-12-2021", category = Some(5)))), "Client2") // both same hotel on same date, one should fail
  val ClientActor3 = system.actorOf(Props(new Client(SystemActor, "amber", 19, "345678", Query("apartment", "25-12-2021"))), "Client3")
  val ClientActor4 = system.actorOf(Props(new Client(SystemActor, "alain", 56, "456789", Query("resort", "25-12-2021", Some("Disney")))), "Client4")
  val ClientActor5 = system.actorOf(Props(new Client(SystemActor, "tanja", 56, "567890", Query("apartment", "25-12-2021", category = Some(5)))), "Client5") // could be the same as Client3
}

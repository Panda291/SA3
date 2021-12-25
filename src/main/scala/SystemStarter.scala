import Actors.{Client, ReservationService, SystemService}
import akka.actor.{ActorSystem, Props}

object SystemStarter extends App {
  val system = ActorSystem("AkkaApplication")
  val ReservationActor = system.actorOf(Props[ReservationService], "ReservationService")
  val SystemActor = system.actorOf(Props(new SystemService(ReservationActor)), "SystemService")
  val ClientActor = system.actorOf(Props(new Client(SystemActor)), "Client")
}

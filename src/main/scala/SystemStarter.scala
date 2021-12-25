import Actors.{Client, SystemService}
import akka.actor.{ActorSystem, Props}

object SystemStarter extends App {
  val system = ActorSystem("AkkaApplication")
  val SystemActor = system.actorOf(Props[SystemService], "SystemService")

  val ClientActor = system.actorOf(Props(new Client(SystemActor)), "Client")
}

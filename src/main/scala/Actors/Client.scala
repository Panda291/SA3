package Actors

import Messages.{MakeReservation, Property, ReservationAcknowledged, ReservationConfirmed, ReservationDenied, Search, SearchResult}
import akka.actor.{Actor, ActorRef}

import scala.util.Random

class Client(systemService: ActorRef) extends Actor{
  val random = new Random
  def receive: Receive = {
    case SearchResult(list: List[Property]) =>
      if(list.nonEmpty) {
        systemService ! MakeReservation(list(random.nextInt(list.length)), self)
      } else {
        println("no properties found")
        context.stop(self)
      }
    case ReservationConfirmed(property) =>
      println(s"reservation confirmed: $property")
      sender ! ReservationAcknowledged
    case ReservationDenied(property) =>
      println(s"reservation denied: $property")
      sender ! ReservationAcknowledged
    case msg => println(s"unrecognized message: $msg")
  }

  systemService ! Search("hotel", "25-12-2021", self) // test call to check searching
}

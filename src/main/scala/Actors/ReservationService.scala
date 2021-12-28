package Actors

import Messages.{AllProperties, MakeReservation, Property, ReservationConfirmed, ReservationDenied, SearchResult}
import akka.actor.Actor

class ReservationService extends Actor{
    var properties: List[Property] = List(
      Property(5, "test", "hotel", 5, ("Belgium", "Brussels"), "25-12-2021"),
      Property(6, "test2", "hotel", 3, ("Belgium", "Antwerp"), "25-12-2021"),
      Property(7, "test3", "apartment", 4, ("France", "Paris"), "25-12-2021")
    )
  def receive: Receive = {
    case AllProperties =>
//      println("reservation service")
      sender ! SearchResult(properties)
    case MakeReservation(property, _) =>
      if(properties.contains(property)) {
        properties = properties.filter(_ != property)
        sender ! ReservationConfirmed(property)
      } else sender ! ReservationDenied(property)
    case msg => println(s"unrecognized message: $msg")
  }
}

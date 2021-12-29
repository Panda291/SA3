package Actors

import Messages._
import akka.actor.Actor

import scala.collection.mutable

class ReservationService extends Actor{
    var properties: List[Property] = List(
      Property(5, "test", "hotel", 4, ("Belgium", "Brussels")),
      Property(6, "test2", "apartment", 3, ("Belgium", "Antwerp")),
      Property(7, "Disney Land", "resort", 5, ("France", "Paris")),
      Property(8, "test3", "apartment", 5, ("Belgium", "Gent")),
      Property(9, "test4", "hotel", 3, ("France", "Nice")),
      Property(10, "Nice hotel", "hotel", 5, ("Germany", "Berlin"))
    )
  val reservations: mutable.Map[Property, List[String]] = mutable.Map().withDefaultValue(List())

  def receive: Receive = {
    case AllProperties =>
//      println("reservation service")
      sender ! SearchResult(properties)
    case AllReservations =>
      sender ! AllReservations(reservations)
    case MakeReservation(property, date, _) =>
      if(properties.contains(property) && !reservations(property).contains(date)) { // property exists and is not taken on this date
        reservations(property) = date :: reservations(property)
        sender ! ReservationConfirmed(property, date)
      } else {
        sender ! ReservationDenied(property, date)
      }
    case msg => println(s"unrecognized message: $msg")
  }
}

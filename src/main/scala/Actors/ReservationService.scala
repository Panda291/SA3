package Actors

import Messages.{AllProperties, Property, SearchResult}
import akka.actor.Actor

class ReservationService extends Actor{
    val properties: List[Property] = List(
      Property(5, "test", "hotel", 5, ("Belgium", "Brussels"), "25-12-2021"),
      Property(6, "test2", "hotel", 3, ("Belgium", "Antwerp"), "25-12-2021")
    )
  def receive: Receive = {
    case AllProperties =>
//      println("reservation service")
      sender ! SearchResult(properties)
    case msg => println(s"unrecognized message: $msg")
  }
}

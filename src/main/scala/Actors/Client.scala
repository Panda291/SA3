package Actors

import Messages.{MakeReservation, Property, Query, ReservationAcknowledged, ReservationConfirmed, ReservationDenied, Search, SearchResult}
import akka.actor.{Actor, ActorRef}

import scala.util.Random

class Client(systemService: ActorRef, username: String, age: Int, passport: String, query: Query) extends Actor{
  val random = new Random
  def receive: Receive = {
    case SearchResult(list: List[Property]) =>
      if(list.nonEmpty) {
        systemService ! MakeReservation(list(random.nextInt(list.length)), query.date, self)
      } else {
        println(s"$username: no properties found")
        context.stop(self)
      }
    case ReservationConfirmed(property, date) =>
      println(s"$username: reservation confirmed: $property on: $date")
      sender ! ReservationAcknowledged
    case ReservationDenied(property, date) =>
      println(s"$username: reservation denied: $property on $date")
      sender ! ReservationAcknowledged
      systemService ! query.toSearch(self) // if the chosen property was chosen before you got the chance, repeat the process from the start
    case msg => println(s"unrecognized message: $msg")
  }

  systemService ! query.toSearch(self) // test call to check searching
}

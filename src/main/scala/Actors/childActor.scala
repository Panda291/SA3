package Actors

import Messages.{CacheInvalidated, MakeReservation, Property, ReservationAcknowledged, ReservationConfirmed, ReservationDenied}
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}

class childActor(property: Property, client: ActorRef, systemService: ActorRef, reservationService: ActorRef) extends Actor {
  def receive: Receive = {
    case msg: ReservationConfirmed =>
      systemService ! msg
      client ! msg
    case msg: ReservationDenied =>
      systemService ! CacheInvalidated
      client ! msg
    case ReservationAcknowledged => context.stop(self)
    case msg => println(s"unrecognized message: $msg")
  }

  reservationService ! MakeReservation(property, client)

}

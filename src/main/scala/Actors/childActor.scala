package Actors

import Messages.{CacheInvalidated, MakeReservation, Property, ReservationAcknowledged, ReservationConfirmed, ReservationDenied}
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}

class childActor(makeReservation: MakeReservation, systemService: ActorRef, reservationService: ActorRef) extends Actor {
  def receive: Receive = {
    case msg: ReservationConfirmed =>
      systemService ! msg
      makeReservation.replyTo ! msg
    case msg: ReservationDenied =>
      systemService ! CacheInvalidated
      makeReservation.replyTo ! msg
    case ReservationAcknowledged => context.stop(self)
    case msg => println(s"unrecognized message: $msg")
  }

  reservationService ! makeReservation

}

package Actors

import Messages.{MakeReservation, Property, ReservationAcknowledged, ReservationConfirmed, ReservationDenied}
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}

class childActor(property: Property, client: ActorRef, reservationService: ActorRef) extends Actor {
  def receive: Receive = {
    case msg: ReservationConfirmed => client ! msg
    case msg: ReservationDenied => client ! msg
    case ReservationAcknowledged => context.stop(self)
    case msg => println(s"unrecognized message: $msg")
  }

  reservationService ! MakeReservation(property, client)

}

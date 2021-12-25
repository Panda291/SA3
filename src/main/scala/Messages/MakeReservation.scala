package Messages

import akka.actor.ActorRef

case class MakeReservation(property: Property, replyTo: ActorRef)

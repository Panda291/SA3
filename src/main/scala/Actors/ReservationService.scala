package Actors

import akka.actor.Actor

class ReservationService extends Actor{
  def receive: Receive = {
    case msg => println(msg)
  }
}

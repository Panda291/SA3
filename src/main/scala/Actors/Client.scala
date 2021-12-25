package Actors

import Messages.{Property, Search, SearchResult}
import akka.actor.{Actor, ActorRef}

class Client(systemService: ActorRef) extends Actor{
  def receive: Receive = {
    case SearchResult(list: List[Property]) =>
      // pick random and reserve at systemService
    case msg => println(msg)
  }

  systemService ! Search("hotel", "25-12-2021", category = Some(3)) // test call to check searching
}

package Actors

import Messages.{Property, Search, SearchResult}
import akka.actor.{Actor, ActorRef}

class Client(systemService: ActorRef) extends Actor{
  def receive: Receive = {
    case SearchResult(list: List[Property]) =>
      println(list)
    case msg => println(s"unrecognized message: $msg")
  }

  systemService ! Search("hotel", "25-12-2021", self, category = Some(4)) // test call to check searching
}

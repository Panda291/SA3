package Messages

import akka.actor.ActorRef

case class Query(propertyType: String, date: String, name: Option[String] = None, category: Option[Int] = None, location: Option[(String, String)] = None) {
  def toSearch(ref: ActorRef): Search = {
    Search(propertyType, date, ref, name, category, location)
  }
}

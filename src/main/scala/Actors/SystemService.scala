package Actors

import Messages.{AllProperties, CacheInvalidated, MakeReservation, Property, ReservationConfirmed, Search, SearchResult}
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class SystemService(reservationService: ActorRef) extends Actor {
  implicit val executionContext = context.dispatcher
  implicit val timeout = Timeout(5 seconds)
  var propertiesCache: List[Property] = List()

  self ! CacheInvalidated

  def receive: Receive = {
    case Search(propertyType, date, replyTo, name, category, location) =>
              replyTo ! SearchResult(
                propertiesCache.filter(_.propertyType == propertyType)
                  .filter(_.date == date)
                  .filter(_.name.contains(name.getOrElse("")))
                  .filter(_.category >= category.getOrElse(0)) // if i say that the category the client is looking for should be greater than or equal to the asked one, i can cleanly solve the option issue. otherwise i would need to implement weirder statements
                  .filter({ property =>
                    val (country, city) = location.getOrElse(("", ""))
                    property.location._1.contains(country) &&
                      property.location._2.contains(city)
                  }))
    case MakeReservation(property, replyTo) =>
      context.system.actorOf(Props(new childActor(property, replyTo, self, reservationService)), "childActor")
    case CacheInvalidated =>
      val propertiesFuture: Future[Any] = reservationService ? AllProperties
      propertiesFuture.map({ // filter the future of all available properties to fit the client query
        case SearchResult(properties) =>
          propertiesCache = properties
        case msg => println(s"test: $msg")
      })
    case ReservationConfirmed(property) =>
      propertiesCache = propertiesCache.filter(_ != property)
    case msg => println(s"unrecognized message: $msg")
  }
}

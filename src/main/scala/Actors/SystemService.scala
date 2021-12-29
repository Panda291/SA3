package Actors

import Messages._
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class SystemService(reservationService: ActorRef) extends Actor {
  implicit val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit val timeout: Timeout = Timeout(5 seconds)

  var propertiesCache: List[Property] = List()
  var reservationCache: mutable.Map[Property, List[String]] = mutable.Map()

  self ! CacheInvalidated

  def receive: Receive = {
    case Search(propertyType, date, replyTo, name, category, location) =>
      replyTo ! SearchResult(
        propertiesCache.filter(_.propertyType == propertyType)
          .filter(_.name.contains(name.getOrElse("")))
          .filter(_.category >= category.getOrElse(0)) // if i say that the category the client is looking for should be greater than or equal to the asked one, i can cleanly solve the option issue. otherwise i would need to implement weirder statements
          .filter({ property =>
            val (country, city) = location.getOrElse(("", ""))
            property.location._1.contains(country) &&
              property.location._2.contains(city)
          })
          .filter(!reservationCache(_).contains(date))) // the chosen date is not taken
    case makeReservation: MakeReservation =>
      context.system.actorOf(Props(new childActor(makeReservation, self, reservationService)))
    case CacheInvalidated =>
      val propertiesFuture: Future[Any] = reservationService ? AllProperties
      propertiesFuture.map({
        case SearchResult(properties) =>
          propertiesCache = properties
        case msg => println(s"unknown message: $msg")
      })
      val reservationsFuture: Future[Any] = reservationService ? AllReservations
      reservationsFuture.map({
        case AllReservations(reservations) =>
          reservationCache = reservations
        case msg => println(s"unknown message: $msg")
      })
    case ReservationConfirmed(property, date) =>
      reservationCache(property)  = date :: reservationCache(property)
    case msg => println(s"unrecognized message: $msg")
  }
}

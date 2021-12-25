package Actors

import Messages.{Property, Search}
import akka.actor.Actor

import java.time.LocalDate

class SystemService extends Actor {
  val properties: List[Property] = List(
    Property(5, "test", "hotel", 5, ("Belgium", "Brussels"), "25-12-2021"),
    Property(6, "test2", "hotel", 3, ("Belgium", "Antwerp"), "25-12-2021")
  )
  def receive: Receive = {
    case Search(propertyType, date, name, category, location) =>
      sender ! properties.filter(_.propertyType == propertyType)
        .filter(_.date == date)
        .filter(_.name.contains(name.getOrElse("")))
        .filter(_.category >= category.getOrElse(0)) // if i say that the category the client is looking for should be greater than or equal to the asked one, i can cleanly solve the option issue. otherwise i would need to implement weirder statements
        .filter({property =>
          val (country, city) = location.getOrElse(("", ""))
          property.location._1.contains(country) &&
            property.location._2.contains(city)
        })
    case msg => println(msg)
  }
}

package Messages

import scala.collection.mutable

case class AllReservations(reservations: mutable.Map[Property, List[String]] = mutable.Map())

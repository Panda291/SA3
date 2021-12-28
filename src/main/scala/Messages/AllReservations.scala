package Messages

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

case class AllReservations(reservations: mutable.Map[Property, List[String]] = mutable.Map())

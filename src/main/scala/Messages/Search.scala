package Messages

case class Search(propertyType: String, date: String, name: Option[String] = None, category: Option[Int] = None, location: Option[(String, String)] = None)
// propertyType: mandatory, "hotel", "apartment" or "resort"
// date: mandatory, the date of the wanted reservation (let's do only a day for now) TODO: allow the system to work on date ranges
// name: optional, the name of the place of stay
// category: optional, 3, 4 or 5 stars
// location: optional, tuple of country and city

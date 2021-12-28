package Messages

case class Property(id: Int, name: String, propertyType: String, category: Int, location: (String, String))

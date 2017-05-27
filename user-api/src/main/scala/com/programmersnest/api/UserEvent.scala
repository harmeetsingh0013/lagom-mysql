package com.programmersnest.api

import com.programmersnest.api.models.User
import play.api.libs.json.{Format, Json, __}
import julienrf.json.derived

/**
  * Created by harmeet on 13/5/17.
  */
sealed trait UserEvent {
  val userId: String
}

case class UserAdded(userId: String, user: User) extends UserEvent
object UserAdded {
  implicit val format: Format[UserAdded] = Json.format
}

/**
  * UserEvent companion providing JSON serialization for all event types
  */
object UserEvent {
  implicit val format: Format[UserEvent] =
    derived.flat.oformat((__ \ "type").format[String])
}
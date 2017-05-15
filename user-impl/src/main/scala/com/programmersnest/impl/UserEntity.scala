package com.programmersnest.impl

import java.util.Date

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger, PersistentEntity}
import com.programmersnest.api.models.User
import play.api.libs.json.{Format, Json}

/**
  * Created by harmeet on 9/5/17.
  */
class UserEntity extends PersistentEntity {

  override type Command = UserCommand
  override type Event = UserEvent
  override type State = UserEntityState

  override def initialState: UserEntityState = UserEntityState(None, None, timestamp = (new Date).getTime)

  override def behavior: Behavior = Actions()
  .onCommand[AddNewUser, Done] {
    case (AddNewUser(user), ctx, state) =>
      ctx.thenPersist(UserAdded(entityId, user)) {_ => ctx.reply(Done)}
  }
}

sealed trait UserCommand

case class AddNewUser(user: User) extends UserCommand with ReplyType[Done]
object AddNewUser {
  implicit val format: Format[AddNewUser] = Json.format
}

/*
 * User module Event Implementation
 */
sealed trait UserEvent extends AggregateEvent[UserEvent] {
  override def aggregateTag: AggregateEventTagger[UserEvent] = UserEvent.INSTANCE
}
object UserEvent {
  val INSTANCE = AggregateEventTag[UserEvent]
}

case class UserAdded(userId: String, user: User) extends UserEvent
object UserAdded {
  implicit val format: Format[UserAdded] = Json.format
}
/*
 * User module State Implementation
 */

case class UserEntityState (userId: Option[String], user: Option[User], timestamp: Long)
object UserEntityState {
  implicit val format: Format[UserEntityState] = Json.format
}
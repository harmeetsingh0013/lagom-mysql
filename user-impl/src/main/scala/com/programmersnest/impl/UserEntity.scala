package com.programmersnest.impl

import java.sql.Timestamp
import java.util.Date

import akka.Done
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger, PersistentEntity}
import com.programmersnest.api.models.User
import play.api.libs.json.{Format, Json}

import scala.util.Try

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
  }.onReadOnlyCommand[UserDetail, User] {
    case (UserDetail(id), ctx, state) =>
      Try(state.user).toOption.getOrElse(throw NotFound(s"user with ${id} not found"))
  }.onEvent {
    case (UserAdded(userId, user), state) =>
      UserEntityState(Some(userId), Some(user), System.currentTimeMillis())
  }
}

sealed trait UserCommand

case class AddNewUser(user: User) extends UserCommand with ReplyType[Done]
object AddNewUser {
  implicit val format: Format[AddNewUser] = Json.format[AddNewUser]
}

case class UserDetail(id: String) extends UserCommand with ReplyType[User]
object UserDetail {
  implicit val format: Format[UserDetail] = Json.format[UserDetail]
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
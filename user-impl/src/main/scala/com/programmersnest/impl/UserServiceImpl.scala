package com.programmersnest.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.programmersnest.api
import com.programmersnest.api.UserService
import com.programmersnest.api.models.User
import com.programmersnest.utility.{Constant, Utility}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by harmeet on 6/5/17.
  */
class UserServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends UserService {

  private def systemRefFor(systemId: String) = registry.refFor[UserEntity](systemId)

  override def addOrUpdateUser: ServiceCall[User, Done] = ServerServiceCall { user =>
    val newUser = user.copy(id = Some(Utility.generateUUID), status = Some(Constant.INACTIVE))
    systemRefFor(newUser.id.get).ask(AddNewUser(newUser))
  }

  override def findAllUsers(limit: Int): ServiceCall[NotUsed, Vector[User]] = ServerServiceCall {_ =>
    Future.successful(Vector.empty)
  }

  override def findUserDetail(id: String): ServiceCall[NotUsed, User] = ServerServiceCall{_ =>
    Future.successful(throw NotFound(" ------ "))
  }

  override def searchUsers(name: Option[String], limit: Option[Int], sort: Option[String]):
    ServiceCall[NotUsed, Vector[User]] = ServerServiceCall {_ =>
    Future.successful(Vector.empty)
  }

  override def uploadUsersFromCSV: ServiceCall[NotUsed, Done] = ServerServiceCall{_ =>
    Future.successful(Done)
  }

  override def events = TopicProducer.singleStreamWithOffset { offset =>

    registry.eventStream(UserEvent.INSTANCE, offset) map (events => (eventsResolver(events), offset))
  }

  private def eventsResolver(userEvents: EventStreamElement[UserEvent]): api.UserEvent = {
    userEvents.event match {
      case UserAdded(userId, user) => api.UserAdded(userId, user)
    }
  }
}

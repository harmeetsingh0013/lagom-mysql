package com.programmersnest.impl

import java.sql.Timestamp

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
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by harmeet on 6/5/17.
  */
class UserServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends UserService {

  private val log = LoggerFactory.getLogger(classOf[UserServiceImpl])
  private def systemRefFor(systemId: String) = registry.refFor[UserEntity](systemId)

  override def addNewUser: ServiceCall[User, Done] = ServerServiceCall { user =>
    val newUser = user.copy(id = Some(Utility.generateUUID),
      date = Some(new Timestamp(System.currentTimeMillis())), status = Some(Constant.INACTIVE))

    log.info(s"New user with ${newUser.id.get} id will added.")
    systemRefFor(newUser.id.get).ask(AddNewUser(newUser))
  }

  override def findAllUsers(limit: Int): ServiceCall[NotUsed, Vector[User]] = ServerServiceCall { _ =>
    Future.successful(Vector.empty)
  }

  override def findUserDetail(id: String): ServiceCall[NotUsed, User] = ServerServiceCall { _ =>
    log.info(s"find $id user detail ... ")
    systemRefFor(id).ask(UserDetail(id)) map {
      case Some(user) => user
      case None => throw NotFound(s"User with ${id} id not found.")
    }
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

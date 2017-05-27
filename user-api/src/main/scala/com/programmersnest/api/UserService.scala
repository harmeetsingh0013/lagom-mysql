package com.programmersnest.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.Method.POST
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.programmersnest.api.models.User

/**
  * Created by harmeet on 6/5/17.
  */
trait UserService extends Service {

  def addNewUser: ServiceCall[User, Done]

  def findAllUsers(limit: Int): ServiceCall[NotUsed, Vector[User]]

  def findUserDetail(id: String): ServiceCall[NotUsed, User]

  def searchUsers(name: Option[String], limit: Option[Int],
                  sort: Option[String]): ServiceCall[NotUsed, Vector[User]]

  def uploadUsersFromCSV: ServiceCall[NotUsed, Done]

  def events: Topic[UserEvent]

  override def descriptor: Descriptor = {
    import Service._

    named("user").withCalls(
      restCall(POST, "/api/user", addNewUser _),
      pathCall("/api/users?limit", findAllUsers _),
      pathCall("/api/user/:id", findUserDetail _),
      pathCall("/api/users/search?name&limit&sort", searchUsers _),
      restCall(POST, "/api/users/upload", uploadUsersFromCSV _)
    ).withTopics(
      topic(UserKafkaTopic.NAME, this.events)
    ).withAutoAcl(true)
  }
}

object UserKafkaTopic {
  val NAME = "user-events"
}

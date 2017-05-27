package com.programmersnest.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.programmersnest.api.models.Item

/**
  * Created by harmeet on 6/5/17.
  */
trait ItemService extends Service {

  def addOrUpdateItem: ServiceCall[Item, Done]

  def findAllItem(limit: Int): ServiceCall[NotUsed, Seq[Item]]

  def findItem(id: UUID): ServiceCall[NotUsed, Item]

  def uploadItemsFromCSV: ServiceCall[NotUsed, Done]

  def searchItems(name: Option[String], date: Option[Long], limit: Option[Int], sort: Option[String]): ServiceCall[NotUsed, Seq[Item]]

  override def descriptor: Descriptor = {
    import Service._

    named("item").withCalls(
      restCall(Method.POST, "/api/item", addOrUpdateItem _),
      pathCall("/api/items?limit", findAllItem _),
      pathCall("/api/item/:id", findItem _),
      restCall(Method.POST, "/api/items/upload", uploadItemsFromCSV _),
      pathCall("/api/items/search?name&data&limit&sort", searchItems _)
    ).withAutoAcl(true)
  }
}

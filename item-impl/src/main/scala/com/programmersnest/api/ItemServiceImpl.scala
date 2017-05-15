package com.programmersnest.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.programmersnest.api.models.Item

/**
  * Created by harmeet on 6/5/17.
  */
class ItemServiceImpl extends ItemService {

  override def addOrUpdateItem: ServiceCall[Item, Done] = ???

  override def findAllItem(limit: Int): ServiceCall[NotUsed, Seq[Item]] = ???

  override def findItem(id: UUID): ServiceCall[NotUsed, Item] = ???

  override def uploadItemsFromCSV: ServiceCall[NotUsed, Done] = ???

  override def searchItems(name: Option[String], date: Option[Long], limit: Option[Int], sort: Option[String]): ServiceCall[NotUsed, Seq[Item]] = ???
}

package com.programmersnest.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.programmersnest.api.models.{Order, OrderItem}

/**
  * Created by harmeet on 7/5/17.
  */
trait OrderService extends Service {

  def addOrUpdateOrder: ServiceCall[Order, Done]

  def findCustomerOrders(customerId: UUID, limit: Int): ServiceCall[NotUsed, Vector[Order]]

  def findOrderItems(orderId: UUID): ServiceCall[NotUsed, Vector[OrderItem]]

  def orderStatusUpdate(orderId: UUID): ServiceCall[Map[String, String], Done]

  def searchOrders(customerId: Option[UUID], from: Option[Long], to: Option[Long], limit: Option[Int], sort: Option[String]): ServiceCall[NotUsed, Vector[Order]]

  override def descriptor: Descriptor = {
    import Service._

    named("order").withCalls(
      restCall(Method.POST, "/api/order", addOrUpdateOrder _),
      pathCall("/api/customer/:customerId/orders?limit", findCustomerOrders _),
      pathCall("/api/order/:orderId/items", findOrderItems _),
      restCall(Method.PUT, "/api/order", orderStatusUpdate _),
      pathCall("/api/orders?customerId&from&to&limit&sort", searchOrders _)
    )
  }
}

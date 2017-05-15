package com.programmersnest.api.models

import java.util.UUID

import play.api.libs.json.Json

/**
  * Created by harmeet on 7/5/17.
  */
case class OrderItem(
                       orderId: Option[UUID] = None,
                       itemId: UUID,
                       itemQty: Int,
                       itemPrice: Double
                     )

object OrderItem {
  implicit val formatter = Json.format[OrderItem]
}
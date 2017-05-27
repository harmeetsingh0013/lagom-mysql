package com.programmersnest.api.models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import play.api.libs.json._

/**
  * Created by harmeet on 7/5/17.
  */
case class Order(
                  id: Option[UUID] = None,
                  date: Timestamp,
                  status: Option[String] = None,
                  customerId: UUID,
                  discount: Option[Double] = None,
                  tax: Double,
                  items: Vector[OrderItem] = Vector.empty
                )

object Order {
  implicit object timestampFormat extends Format[Timestamp] {
    def reads(json: JsValue) = JsSuccess(new Timestamp(json.as[Long]))
    def writes(ts: Timestamp) = JsString(ts.getTime.toString)
  }

  implicit val formatter = Json.format[Order]
}
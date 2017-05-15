package com.programmersnest.api.models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import play.api.libs.json._

/**
  * Created by harmeet on 6/5/17.
  */
case class Item(
                 id: Option[UUID] = None,
                 name: String,
                 price: Double,
                 code: String,
                 qty: Option[Int] = None,
                 date: Option[Timestamp] = None
               )

object Item {
  implicit object timestampFormat extends Format[Timestamp] {
    def reads(json: JsValue) = JsSuccess(new Timestamp(json.as[Long]))
    def writes(ts: Timestamp) = JsString(ts.getTime.toString)
  }

  implicit val formatter = Json.format[Item]
}
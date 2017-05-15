package com.programmersnest.api.models

import java.sql.Timestamp

import play.api.libs.json._

/**
  * Created by harmeet on 6/5/17.
  */
case class User(
                 id: Option[String] = None,
                 date: Option[Timestamp] = None,
                 name: String,
                 email: String,
                 mobile: String,
                 address: Option[String] = None,
                 city: Option[String] = None,
                 state: Option[String] = None,
                 pinNo: Option[String] = None,
                 company: String,
                 vatNo: String,
                 status: Option[String] = None
               )

object User {
  implicit object timestampFormat extends Format[Timestamp] {
    def reads(json: JsValue) = JsSuccess(new Timestamp(json.as[Long]))
    def writes(ts: Timestamp) = JsString(ts.getTime.toString)
  }

  implicit val formatter = Json.format[User]
}

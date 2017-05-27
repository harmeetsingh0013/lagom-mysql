package com.programmersnest.impl.repository

import akka.Done
import com.lightbend.lagom.scaladsl.api.transport.{ExceptionMessage, NotAcceptable, TransportErrorCode}
import com.lightbend.lagom.scaladsl.persistence.jdbc.JdbcSession
import com.programmersnest.api.models.User

import scala.concurrent.Future

/**
  * Created by harmeet on 6/5/17.
  */
class UserRepository(session: JdbcSession) {

  import JdbcSession.tryWith

  def addNewUser(user: User): Future[Done] = {
    val sql =
      """
        |INSERT INTO users (id, date, name, mobile_1, company, vat_no, pin_no)
        |VALUES
        |(?, ?, ?, ?, ?, ?, ?)
        |ON DUPLICATE KEY UPDATE id=?;
      """.stripMargin

    session.withConnection(con => {
      tryWith(con.prepareStatement(sql)) { statement => {
        statement.setString(1, user.id.get)
        statement.setTimestamp(2, user.date.get)
        statement.setString(3, user.name)
        statement.setString(4, user.mobile1)
        statement.setString(5, user.company)
        statement.setString(6, user.vatNo)
        statement.setString(7, user.pinNo)
        statement.setString(8, user.id.get)
        if(!statement.execute()) Done else throw
          new NotAcceptable(TransportErrorCode.UnsupportedData, new ExceptionMessage("Invalid Data", "User properties invalid"))
      }}
    })
  }
}

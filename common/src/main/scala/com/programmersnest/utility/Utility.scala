package com.programmersnest.utility

import java.util.UUID

/**
  * Created by harmeet on 9/5/17.
  */
object Utility {

  def generateUUID = UUID.randomUUID().toString.replaceAll("-", "")
}

package com.example.payloads

import com.example.util.Patch

case class UpdateNotesRequest(title: Patch[String], body: Option[String])

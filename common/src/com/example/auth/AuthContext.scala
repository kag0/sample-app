package com.example.auth

import java.util.UUID
import java.time.OffsetDateTime

sealed trait AuthContext

case class UserAuthContext(userId: UUID, authenticatedAt: OffsetDateTime)
    extends AuthContext

package com.jhzlo.pretest.user.domain.entity

import com.jhzlo.pretest.auth.dto.SignUpRequest
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import com.jhzlo.pretest.common.shared.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    var userRole: UserRole = UserRole.MEMBER,
) : BaseEntity() {
    companion object{
        fun of(signUp: SignUpRequest, password: String): User {
            return User(
                email = signUp.email,
                name = signUp.userName,
                password = password
            )
        }
    }
}

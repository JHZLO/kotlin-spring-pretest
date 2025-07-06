package com.jhzlo.pretest.chat.domain.entity

import com.jhzlo.pretest.common.shared.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "chats")
class Chat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    val chatThread: ChatThread,

    @Column(columnDefinition = "TEXT")
    val question: String,

    @Column(columnDefinition = "TEXT")
    val answer: String,
) : BaseEntity() {

}

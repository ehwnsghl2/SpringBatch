package com.brandjunhoe.springbatch

import javax.persistence.*


@Entity
@Table(name = "member")
class Member(

    val name: String,

    val email: String,

    val nickName: String,

    val state: String,

    var amount: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

)
package com.muratguzel.instakotlin.Login.model

class UserDetails {
    var follower: String? = null
    var following: String? = null
    var post: String? = null
    var profileImage: String? = null
    var biograpyh: String? = null
    var website: String? = null

    constructor()
    constructor(
        website: String?,
        biograpyh: String?,
        profileImage: String?,
        post: String?,
        following: String?,
        follower: String?,
    ) {
        this.website = website
        this.biograpyh = biograpyh
        this.profileImage = profileImage
        this.post = post
        this.following = following
        this.follower = follower
    }

    override fun toString(): String {
        return "UserDetails(fallower=$follower, fallowing=$following, post=$post, profileImage=$profileImage, biograpyh=$biograpyh, website=$website)"
    }
}
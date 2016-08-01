package jd.tutorial.blog

class Model {

    data class Post(val id: Int, val title: String, val categories: List<String>, val content: String)

    private var nextId = 1
    private val posts = hashMapOf<Int, Post>()

    fun createPost(title: String, categories: List<String>, content: String): Int {
        val id = nextId++
        val post = Post(id = id, title = title, categories = categories, content = content)
        posts.put(id, post)
        return id
    }

    fun getAllPosts() = posts.keys.sorted().map {
        posts[it]
    }

}

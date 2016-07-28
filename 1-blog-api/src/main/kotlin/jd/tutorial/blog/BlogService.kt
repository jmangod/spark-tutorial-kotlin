package jd.tutorial.blog

import com.google.gson.Gson
import spark.Spark.*

object BlogService {

    val HTTP_BAD_REQUEST = 400
    val TYPE_APPLICATION_JSON = "application/json"

    val model = Model()

    @JvmStatic
    fun main(args: Array<String>) {
        setupExceptionHandler()
        setupRoutes()
        System.out.println("API is up and running at http://localhost:4567\n")
    }

    private fun setupExceptionHandler() {
        exception(Exception::class.java) { exception, request, response ->
            // TODO : logging
            System.out.println(exception)
            response.body("OOH! something went wrong. Please contact JD.\n")
        }
    }

    private fun setupRoutes() {

        post("/posts") { request, response ->
            try {
                val postPayload = Gson().fromJson(request.body(), NewPostPayload::class.java)
                if (postPayload.isValid()) {
                    val id = model.createPost(title = postPayload.title,
                            categories = postPayload.categories,
                            content = postPayload.content)
                    response.status(200)
                    response.type(TYPE_APPLICATION_JSON)
                    id
                } else {
                    System.out.println("Invalid payload")
                    response.status(HTTP_BAD_REQUEST)
                    "Bad Request"
                }
            } catch (e: Exception) {
                System.out.println(e)
                response.status(HTTP_BAD_REQUEST)
                "Bad Request"
            }
        }

        get("/posts") { request, response ->
            response.status(200)
            response.type(TYPE_APPLICATION_JSON)
            val posts = model.getAllPosts()
            val gson = Gson()
            gson.toJson(posts)
        }

    }

}

data class NewPostPayload(val title: String, val categories: List<String>, val content: String) {
    fun isValid() = title.isNotBlank() && categories.isNotEmpty()
}


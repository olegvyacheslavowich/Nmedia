package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.model.like.Like
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.util.Action
import java.lang.IllegalArgumentException
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val tagUnknownAction = "Unknown action"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channelLikes = NotificationChannel(
                "1",
                getString(R.string.like_channel), importance
            ).apply {
                description = getString(R.string.like_channel_descriprion_text)
            }

            val channelNewPost = NotificationChannel(
                "2",
                getString(R.string.new_post_channel), importance
            ).apply {
                description = getString(R.string.new_post_channel_description)
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channelLikes)
            manager.createNotificationChannel(channelNewPost)

        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data[action]?.let {
            try {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(message.data[content], Like::class.java)
                    )
                    Action.POST -> handlePost(
                        gson.fromJson(message.data[content], Post::class.java)
                    )
                }
            } catch (e: IllegalArgumentException) {
                Log.i(tagUnknownAction, it)
            }
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_netology_foreground)
            .setContentTitle(
                getString(R.string.user_liked_notification, content.userName, content.postAuthor)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    fun handlePost(content: Post) {
        val notification = NotificationCompat.Builder(this, "2")
            .setSmallIcon(R.drawable.ic_launcher_netology_foreground)
            .setContentTitle(
                getString(R.string.new_post_notification, content.author)
            )
            .setContentText(content.content)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(content.content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

}
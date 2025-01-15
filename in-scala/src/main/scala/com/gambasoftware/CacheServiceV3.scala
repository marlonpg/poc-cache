package com.gambasoftware

import java.util.concurrent.{Executors, ScheduledFuture, TimeUnit}
import scala.collection.concurrent.TrieMap

/*
  Changed it from class to object
  Singleton: Only one instance exists. It's like a class that cannot be instantiated more than once.
  Shared State: Often used for utility methods, constants, or to hold state that should be shared among all parts of your application.
  No Parameters: You can't pass parameters to an object like you can with class constructors.
 */
object CacheServiceV3 {
  private val cache = TrieMap[String, Any]()
  private var ttl: Long = 13000L
  private val scheduler = Executors.newScheduledThreadPool(1)
  private val scheduledTasks = TrieMap[String, ScheduledFuture[_]]()

  def put(key: String, value: Any): Unit = synchronized {
    cache.put(key, value)

    // If there's already a scheduled invalidation for this key, cancel it
    scheduledTasks.get(key).foreach(_.cancel(false))

    // Schedule the invalidation of the cache entry
    val task = scheduler.schedule(new Runnable {
      override def run(): Unit = {
        cache.remove(key)
        scheduledTasks.remove(key)
        println(s"Cache entry for key $key invalidated")
      }
    }, ttl, TimeUnit.MILLISECONDS)

    scheduledTasks.put(key, task)
  }

  def get(key: String): Option[Any] = synchronized {
    cache.get(key)
  }

  def setTTL(newTTL: Long): Unit = {
    this.ttl = newTTL
  }
}

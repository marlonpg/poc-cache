package com.gambasoftware

import java.util.concurrent.{Executors, ScheduledFuture, TimeUnit}
import scala.collection.mutable

class Cache[K, V] {
  private val cache = mutable.Map[K, V]()
  private val scheduler = Executors.newScheduledThreadPool(1)
  private val scheduledTasks = mutable.Map[K, ScheduledFuture[_]]()

  def put(key: K, value: V, ttl: Long): Unit = {
    // Add the item to the cache
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

  def get(key: K): Option[V] = cache.get(key)
}

object Main extends App {
  val cache = new Cache[String, String]()
  cache.put("key1", "value1", 5000)  // TTL of 5 seconds
  Thread.sleep(10000)
  println(cache.get("key1"))  // Should print None as the entry is invalidated
}


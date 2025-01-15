package com.gambasoftware

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._
/*
  Changed it from class to object
  Singleton: Only one instance exists. It's like a class that cannot be instantiated more than once.
  Shared State: Often used for utility methods, constants, or to hold state that should be shared among all parts of your application.
  No Parameters: You can't pass parameters to an object like you can with class constructors.
 */
object CacheServiceV2 {
  private var ttl: Long = 13000L
  private val cache = new ConcurrentHashMap[String, Any]().asScala
  private val expirationTimes = new ConcurrentHashMap[String, Long]().asScala

  def put(key: String, value: Any): Unit = synchronized {
    cache.put(key, value)
    expirationTimes.put(key, System.currentTimeMillis() + ttl)
  }

  def get(key: String): Option[Any] = synchronized {
    expirationTimes.get(key) match {
      case Some(expirationTime) if expirationTime < System.currentTimeMillis() =>
        cache.remove(key)
        expirationTimes.remove(key)
        None
      case Some(expirationTime) =>
        cache.get(key)
      case None => cache.get(key)
    }
  }

  def cleanup(): Unit = synchronized {
    val currentTime = System.currentTimeMillis()
    expirationTimes.foreach {
      case (key, expirationTime) if expirationTime < currentTime =>
        cache.remove(key)
        expirationTimes.remove(key)
      case _ =>
    }
  }

  def setTTL(newTTL: Long): Unit = {
    this.ttl = newTTL
  }
}

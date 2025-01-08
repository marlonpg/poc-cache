package com.gambasoftware

import scala.collection.mutable

class CacheService(private var ttl: Long = 13000L) {
  private val cache = mutable.Map[String, Any]()
  private val expirationTimes = mutable.Map[String, Long]()

  def put(key: String, value: Any): Unit = {
    cache.put(key, value)
    expirationTimes.put(key, System.currentTimeMillis() + ttl)
  }

  def get(key: String): Option[Any] = {
    expirationTimes.get(key) match {
      //Expired?
      case Some(expirationTime) if expirationTime < System.currentTimeMillis() =>
        cache.remove(key)
        expirationTimes.remove(key)
        None
      case _ => cache.get(key)
    }
  }
}

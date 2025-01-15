package com.gambasoftware.examples

import com.gambasoftware.{CacheService, CacheServiceV2, CacheServiceV3}

object CacheServiceExample extends App {
  val cache = new CacheService

  cache.put("12345", "Marlon")
  cache.put("54321", "Kira")

  CacheServiceV2.put("12345", "Marlon")
  CacheServiceV2.put("54321", "Kira")

  CacheServiceV3.setTTL(15000)
  CacheServiceV3.put("12345", "Marlon")
  CacheServiceV3.put("54321", "Kira")

  println(cache.get("12345"))
  println(cache.get("54321"))

  println(CacheServiceV2.get("12345"))
  println(CacheServiceV2.get("54321"))

  println(CacheServiceV3.get("12345"))
  println(CacheServiceV3.get("54321"))

  //Testing TTL
  val sleepingTime = 14000L
  println("Sleeping for " + sleepingTime + "ms")
  Thread.sleep(sleepingTime)

  println(cache.get("12345"))
  println(cache.get("54321"))

  println(CacheServiceV2.get("12345"))
  println(CacheServiceV2.get("54321"))

  println(CacheServiceV3.get("12345"))
  println(CacheServiceV3.get("54321"))
}

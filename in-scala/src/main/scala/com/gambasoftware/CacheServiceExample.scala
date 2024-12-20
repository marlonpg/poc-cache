package com.gambasoftware


object CacheServiceExample extends App {
  val cache = new CacheService

  cache.put("12345", "Marlon")
  cache.put("54321", "Kira")

  println(cache.get("12345"))
  println(cache.get("54321"))

  //Testing TTL
  val sleepingTime = 14000L
  println("Sleeping for " + sleepingTime + "ms")
  Thread.sleep(sleepingTime)

  println(cache.get("12345"))
  println(cache.get("54321"))

}

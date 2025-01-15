import com.gambasoftware.{CacheService, CacheServiceV2}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CacheServiceV2Test extends AnyFlatSpec with Matchers {

  "put" should "populate value for a specific key" in {
    val hardCodedNumber = 123456
    CacheServiceV2.put("test", hardCodedNumber)
    val result = CacheServiceV2.get("test")
    result.get shouldEqual hardCodedNumber
  }

  "get expired cache" should "return None for a value that is not valid anymore due to ttl" in {
    val hardCodedNumber = 123456
    CacheServiceV2.setTTL(10L)
    CacheServiceV2.put("test", hardCodedNumber)
    Thread.sleep(11)
    val result = CacheServiceV2.get("test")

    result shouldEqual None
  }

  "get invalid cache" should "return None for a value that is not present inside the cache" in {
    val result = CacheServiceV2.get("test")

    result shouldEqual None
  }
}


import com.gambasoftware.CacheService
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CacheServiceTest extends AnyFlatSpec with Matchers {

  "put" should "populate value for a specific key" in {
    val hardCodedNumber = 123456
    val cache = new CacheService()
    cache.put("test", hardCodedNumber)
    val result = cache.get("test")
    result.get shouldEqual hardCodedNumber
  }

  "get expired cache" should "return None for a value that is not valid anymore due to ttl" in {
    val hardCodedNumber = 123456
    val cache = new CacheService(10L)
    cache.put("test", hardCodedNumber)
    Thread.sleep(11)
    val result = cache.get("test")

    result shouldEqual None
  }
}


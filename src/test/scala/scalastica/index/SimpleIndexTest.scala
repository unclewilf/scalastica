package scalastica.index

import org.specs2.mutable.{Around, Specification}
import org.specs2.execute.AsResult
import org.elasticsearch.node.{NodeBuilder, Node}
import argonaut.{Json, Parse}
import dispatch._, Defaults._
import scalaz._
import Scalaz._
import scala.concurrent.ExecutionContext

class SimpleIndexTest extends Specification {
  implicit val ec:ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val example: Json = Parse.parseOption("{\"hello\" : \"world\"}").get

  def indexExample: Throwable \/ Json = {
    Index.
      index("test-index", "test", example).
      map(\/.fromEither(_).map(Parse.parseOption(_).get)).apply()
  }

  "The elastic search indexer" should {
    "index a simple json snippet" in {
      elastic.around {
        val fieldOrFalse = indexExample.map(_.fieldOrFalse("ok").bool.get)
        fieldOrFalse must be equalTo \/-(true)
      }
    }
  }

}

object elastic extends Around {

  def around[T: AsResult](t: => T) = {
    val node: Node = NodeBuilder.nodeBuilder().node()
    node.client()
    val result = AsResult.effectively(t)
    node.close()
    result
  }

}
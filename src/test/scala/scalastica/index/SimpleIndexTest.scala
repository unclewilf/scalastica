package scalastica.index

import org.specs2.mutable.{Around, Specification}
import org.specs2.execute.AsResult
import org.elasticsearch.node.{NodeBuilder, Node}
import argonaut.{Json, Parse}
import dispatch._, Defaults._

class SimpleIndexTest extends Specification {

  val example: Json = Parse.parseOption("{\"hello\" : \"world\"}").get

  def indexExample: Json = {
    val res = indexer.index("test-index", "test", example).apply()
    res match {
      case Right(x) => Parse.parseOption(x).get
      case Left(_) => throw new RuntimeException
    }
  }

  "The elastic search indexer" should {
    "index a simple json snippet" in {
      elastic.around(
        indexExample.fieldOrFalse("ok").bool.get must beTrue
      )
    }
  }

  object indexer {

    def index(in: String, ty: String, json: Json): Future[Either[Throwable, String]] = {
      val req = url("http://localhost:9200/" + in + "/" + ty + "/").POST
        .setBody(json.toString)
        .addHeader("Content-type", "application/json")
      Http(req OK as.String).either
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
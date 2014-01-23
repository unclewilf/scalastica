package scalastica.index

import argonaut.Json
import dispatch._
import scala.concurrent.ExecutionContext

object Index {
  implicit val ec:ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def index(in: String, ty: String, json: Json): Future[Either[Throwable, String]] = {
    val req = url("http://localhost:9200/" + in + "/" + ty + "/").POST
      .setBody(json.toString)
      .addHeader("Content-type", "application/json")
    Http(req OK as.String).either
  }

}

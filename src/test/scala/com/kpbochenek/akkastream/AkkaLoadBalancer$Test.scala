package com.kpbochenek.akkastream

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.specs2.mutable.Specification

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


class EndpointMock(endpoint: Endpoint) {

  private implicit val system = ActorSystem()
  private implicit val mat = ActorMaterializer()
  private implicit val ec = system.dispatcher
  private val _processed = new AtomicInteger(0)

  private def handler(request: HttpRequest): Future[HttpResponse] = {
    _processed.incrementAndGet()
    Future {
      Thread.sleep(100)
      HttpResponse()
    }
  }

  private val binding = Await.result(Http().bindAndHandleAsync(handler, endpoint.host, endpoint.port), 5.seconds)

  def processed() = _processed.get()
}

class AkkaLoadBalancer$Test extends Specification {
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  "LoadBalancer" should {

    "loadbalance requests over endpoints" in {
      val endpoint1 = Endpoint("localhost", 8001)
      val endpoint2 = Endpoint("localhost", 8002)

      val endpointMock1 = new EndpointMock(endpoint1)
      val endpointMock2 = new EndpointMock(endpoint2)

      val requests = (1 to 10).map(i => HttpRequest())

      val endpointSource = Source(endpoint1 :: endpoint2 :: Nil)
      val requestsSource = Source(requests)

//      val responsesResult = requestsSource.via(AkkaLoadBalancer.flow(endpointSource)).runWith(Sink.seq)

//      val responses = Await.result(responsesResult, 5.seconds)

//      responses.forall(_.isSuccess) must beTrue
//      responses should haveSize(10)

      endpointMock1.processed() should beEqualTo(5)
      endpointMock2.processed() should beEqualTo(5)
    }
  }
}

package com.kpbochenek.akkastream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream._
import akka.stream.javadsl.GraphDSL
import akka.stream.scaladsl.{Flow, Source}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import scala.collection.mutable
import scala.util.Try

case class Endpoint(host: String, port: Int)


class Connection(endpoint: Endpoint) {
  val isReadyToGrab: Boolean = ???

  val isReadyToPush: Boolean = ???

  def push(request: HttpRequest): Unit = ???

  def grabAndPull(): Try[HttpResponse] = ???
}


class LoadBalancerStage extends GraphStage[FanInShape2[Endpoint, HttpRequest, Try[HttpResponse]]] {
  private val endpointsIn = Inlet[Endpoint]("LoadBalancerStage.endpointsIn")
  private val requestsIn = Inlet[HttpRequest]("LoadBalancerStage.requestsIn")
  private val responsesOut = Outlet[Try[HttpResponse]]("LoadBalancerStage.responsesOut")

  override def shape: FanInShape2[Endpoint, HttpRequest, Try[HttpResponse]] = {
    new FanInShape2(endpointsIn, requestsIn, responsesOut)
  }


  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {

    val connections = mutable.Queue.empty[Connection]

    new GraphStageLogic(shape) {

      val endpointsHandler = new InHandler {
        override def onPush(): Unit = handleEndpoint()

        override def onUpstreamFinish(): Unit = {}

        override def onUpstreamFailure(ex: Throwable): Unit = {}
      }

      val requestHandler = new InHandler {
        override def onPush(): Unit = tryHandleRequest()

        override def onUpstreamFinish(): Unit = {}

        override def onUpstreamFailure(ex: Throwable): Unit = {}
      }

      val responseHandler = new OutHandler {
        override def onPull(): Unit = tryHandleResponse()

        override def onDownstreamFinish(): Unit = {}
      }

      def handleEndpoint(): Unit = {
        val endpoint = grab(endpointsIn)
        val connection = new Connection(endpoint)
        connections.enqueue(connection)
        pull(endpointsIn)
      }

      def tryHandleRequest(): Unit = {
        connections.dequeueFirst(_.isReadyToPush).foreach(connection => {
          val request = grab(requestsIn)
          connection.push(request)
        })
      }

      def tryHandleResponse(): Unit = {
        connections.dequeueFirst(_.isReadyToGrab).foreach(connection => {
          val response = connection.grabAndPull()
          push(responsesOut, response)
          connections.enqueue(connection)
        })
      }

      setHandler(endpointsIn, endpointsHandler)
      setHandler(requestsIn, requestHandler)
      setHandler(responsesOut, responseHandler)

    }
  }

}

object AkkaLoadBalancer {
  def flow(endpointSource: Source[Endpoint, NotUsed])(implicit system: ActorSystem) = {
//    Flow.fromGraph(GraphDSL.create(endpointSource)) { implicit builder =>
//      endpoints =>
//
//      val lb = builder.add(new LoadBalancerStage)
//
//      endpoints.out ~> lb.in0
//
//      FlowShape(lb.in1, lb.out)
//    }
  }
}



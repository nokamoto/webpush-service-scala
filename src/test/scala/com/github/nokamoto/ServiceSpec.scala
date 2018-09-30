package com.github.nokamoto

import _root_.webpush.protobuf.Message
import com.github.nokamoto.webpush.WebpushClient
import com.squareup.okhttp.{Protocol, Request, Response}
import io.grpc.{Status, StatusRuntimeException}
import org.mockito.Mockito._
import org.scalatest.AsyncFlatSpec
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future

class ServiceSpec extends AsyncFlatSpec with MockitoSugar {
  private[this] val mockMessage = Message()

  private[this] def mockResponse(code: Int): Response = {
    new Response.Builder()
      .protocol(Protocol.HTTP_1_1)
      .code(code)
      .request(new Request.Builder().url("http://example.com").build())
      .build()
  }

  "Service" should "return empty if push service returns 201" in {
    val client = mock[WebpushClient]
    val sut = new Service(client)

    when(client.sendAsync(mockMessage))
      .thenReturn(Future.successful(mockResponse(201)))

    sut.send(mockMessage).map(_ => succeed)
  }

  "Service" should "return empty if push service returns 500" in {
    val client = mock[WebpushClient]
    val sut = new Service(client)

    when(client.sendAsync(mockMessage))
      .thenReturn(Future.successful(mockResponse(500)))

    sut.send(mockMessage).map(_ => fail("expected error")).recover {
      case e: StatusRuntimeException if e.getStatus == Status.UNIMPLEMENTED =>
        succeed
    }
  }
}

package com.github.nokamoto

import _root_.webpush.protobuf.PushServiceGrpc.PushService
import _root_.webpush.protobuf.Message
import com.github.nokamoto.webpush.WebpushClient
import com.google.protobuf.empty.Empty
import io.grpc.{Status, StatusRuntimeException}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class Service(client: WebpushClient)(implicit ctx: ExecutionContext)
    extends PushService {
  private[this] val created = Future.successful(Empty())

  private[this] val notImplementedYet =
    Future.failed(new StatusRuntimeException(Status.UNIMPLEMENTED))

  override def send(message: Message): Future[Empty] = {
    client
      .sendAsync(message)
      .flatMap { res =>
        res.code() match {
          case ok if ok / 100 == 2 =>
            println(s"$ok - ${res.headers()}")
            created

          case code =>
            println(s"$code not implemented yet: ${res.body().string()}")
            notImplementedYet
        }
      }
      .recoverWith {
        case NonFatal(e) =>
          println(s"unhandled exception: $e")
          e.printStackTrace()
          notImplementedYet
      }
  }
}

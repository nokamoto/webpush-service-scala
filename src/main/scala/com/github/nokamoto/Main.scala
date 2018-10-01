package com.github.nokamoto

import java.util.Base64
import java.util.concurrent.TimeUnit

import com.github.nokamoto.webpush.protobuf.PushServiceGrpc
import com.github.nokamoto.webpush.{KeyPair, WebpushClient}
import com.squareup.okhttp.OkHttpClient
import io.grpc.netty.NettyServerBuilder

import scala.concurrent.ExecutionContext

object Main {
  private[this] val defaultKeyPair = KeyPair(
    privateKey =
      Base64.getDecoder.decode("AJFotoB4FS7IX6tbm5t0SGyISTQ6l54mMzpfYipdOD+N"),
    publicKey = Base64.getDecoder.decode(
      "BNuvjW90TpDawYyxhvK79QVyNEplaSQZOWo1CwXDmWwfya6qnyBvIx3tFvKEBetExvil4rNNRL0/ZR2WLjGEAbQ=")
  )

  case class Configuration(port: Int = 9090,
                           subject: Option[String] = None,
                           keyPair: KeyPair = defaultKeyPair,
                           ctx: ExecutionContext = ExecutionContext.global)

  def main(args: Array[String]): Unit = {
    val cfg = Configuration()
    val client = new WebpushClient(client = new OkHttpClient(),
                                   keyPair = cfg.keyPair,
                                   subject = cfg.subject)

    val server = NettyServerBuilder
      .forPort(cfg.port)
      .addService(
        PushServiceGrpc.bindService(new Service(client)(cfg.ctx), cfg.ctx))
      .build()

    println(s"*** start gRPC server ${cfg.port}")
    server.start()

    sys.addShutdownHook {
      println("*** shutting down gRPC server since JVM is shutting down")
      server.shutdown()
      if (!server.awaitTermination(10, TimeUnit.SECONDS)) {
        server.shutdownNow()
      }
      println("*** server shut down")
    }

    server.awaitTermination()
  }
}

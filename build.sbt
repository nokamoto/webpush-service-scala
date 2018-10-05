scalaVersion := "2.12.6"

name := "webpush-service-scala"

version := IO.readLines(file("VERSION")).head

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.github.nokamoto" %% "webpush-scala" % "0.0.1" exclude ("com.github.nokamoto", "webpush-protobuf"),
  "com.github.nokamoto" %% "webpush-protobuf-grpc" % "0.0.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.22.0" % Test
)

enablePlugins(JavaAppPackaging)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

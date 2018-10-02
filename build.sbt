scalaVersion := "2.12.6"

name := "webpush-service-scala"

version := IO.readLines(file("VERSION")).head

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "com.github.nokamoto" %% "webpush-scala" % "0.0.0-SNAPSHOT" exclude ("com.github.nokamoto", "webpush-protobuf"),
  "com.github.nokamoto" %% "webpush-protobuf-grpc" % "0.0.0-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.22.0" % Test
)

enablePlugins(JavaAppPackaging)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

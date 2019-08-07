name := "zio-play"

version := "0.1"

scalaVersion := "2.12.0"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "dev.zio" %% "zio" % "1.0.0-RC9"

scalacOptions ++= Seq(
  "-Ywarn-value-discard",
  "-Xfatal-warnings"
)
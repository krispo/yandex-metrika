
name :="yandex-metrika"

scalaVersion :="2.10.0"

version :="1.0"

libraryDependencies ++= Seq(
	"play" % "play_2.10" % "2.1.0",
	"org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
	"org.specs2" % "specs2_2.10" % "1.14" % "test")
	
resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)	
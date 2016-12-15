name := "play-java"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  evolutions,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.pac4j" % "play-pac4j" % "2.6.0",
  "org.pac4j" % "pac4j-jwt" % "1.9.4" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-http" % "1.9.4",
  "com.typesafe.play" % "play-cache_2.11" % "2.5.10",
  "commons-io" % "commons-io" % "2.4",
  "be.objectify" %% "deadbolt-java" % "2.5.1"
)

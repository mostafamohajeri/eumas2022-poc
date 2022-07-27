resolvers += ("agent-script" at "http://145.100.135.102:8081/repository/agent-script/").withAllowInsecureProtocol(true)

addSbtPlugin("nl.uva.sne.cci" % "sbt-scriptcc" % "4.41")



addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
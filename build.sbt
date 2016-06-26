resolvers += Resolver.jcenterRepo

libraryDependencies +=
	"org.jtwig" % "jtwig-core" % "5.57"

mainClass in (Compile, run) := Some("com.deigote.oscg.OptimizelyScalaCodeGenerator")

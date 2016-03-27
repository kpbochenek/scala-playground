node {
   // Mark the code checkout 'stage'....
   stage 'Checkout'

   // Get some code from a GitHub repository
   git url: 'https://github.com/kpbochenek/scala-playground.git'

   // Mark the code build 'stage'....
   stage 'Build'

   // Run the maven build
   sh "/var/jenkins_home/tools/hudson.model.JDK/jdk8/bin/java -Dsbt.log.noformat=true -jar /var/jenkins_home/tools/org.jvnet.hudson.plugins.SbtPluginBuilder_SbtInstallation/sbt-local/bin/sbt-launch.jar test"
}
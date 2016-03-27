node {
   // Mark the code checkout 'stage'....
   stage 'Checkout'

   // Get some code from a GitHub repository
   git url: 'https://github.com/kpbochenek/scala-playground.git'

   // Mark the code build 'stage'....
   stage 'Build'

   // Run the maven build
   sh "sbt test"
}
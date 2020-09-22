pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn packae'
  

 
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}

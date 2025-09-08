pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    sh 'chmod +x mvnw'

                    sh './mvnw clean verify'
                }
            }
        }

        stage('SonarQube Analysis & Quality Gate') {
            steps {

                withSonarQubeEnv('IsoAppSonarQube') {
                    sh './mvnw sonar:sonar'
                }


                timeout(time: 1, unit: 'hours') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished. Cleaning up workspace...'
            deleteDir()
        }
    }
}
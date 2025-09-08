
pipeline {

    agent any

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    // WICHTIG: Die Maven Wrapper-Skripte (mvnw), die wir im Repo haben,
                    // müssen auf Linux/Unix-Systemen (wie den meisten Jenkins-Agents)
                    // zuerst ausführbar (executable) gemacht werden.
                    sh 'chmod +x mvnw'

                    /**
                     * Dies ist der Kernbefehl (Aufgabe 3b). Wir nutzen den Wrapper (./mvnw):
                     * 1. clean: Löscht alte Build-Artefakte.
                     * 2. verify: Führt den gesamten Build-Lebenszyklus aus.
                     * DAS BEINHALTET:
                     * a) Das "validate"-Ziel (das unseren Checkstyle-Quality-Check ausführt).
                     * b) Das "compile"-Ziel (baut den Code).
                     * c) Das "test"-Ziel (führt unsere JUnit-Tests aus).
                     *
                     * Wenn Checkstyle ODER ein Test fehlschlägt, schlägt dieser Befehl fehl,
                     * und Jenkins stoppt die Pipeline mit einem "FAILED"-Status.
                     */
                    sh './mvnw clean verify'
                }
            }
        }

//         stage('Quality Check') {
//         }
    }

    post {
        /**
         * "post" Blöcke laufen immer am Ende, unabhängig vom Erfolg.
         * Wir nutzen "always", um das "target"-Verzeichnis aufzuräumen,
         * damit der Workspace für den nächsten Build sauber ist.
         * (Jenkins hat auch eigene Workspace-Cleanup-Plugins, aber das hier ist
         * eine einfache Methode innerhalb der Pipeline.)
         */
        always {
            echo 'Pipeline finished. Cleaning up workspace...'
            // Löscht das 'target'-Verzeichnis, das Build-Artefakte enthält
            deleteDir()
        }
    }
}
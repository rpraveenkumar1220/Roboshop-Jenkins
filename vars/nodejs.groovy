def call() {
    node('workstation') {
        stage('Build') {
            //sh 'npm install'
        }
        stage('Unit Test') {
            echo "Unit testing"
        }
        stage('Code Analysis') {
            echo "Code Analysis"
            // sh 'sudo sonar-scanner -Dsonar.host.url=http://172.31.10.14:9000  -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=cart -Dsonar.qualitygate.wait=true'
        }
        stage('Security Scans') {
            echo "Security Scans"
        }
        if (env.TAG_NAME ==~ ".*") {
            stage('Publish Artifacts') {
                echo "Publish Artifacts"
            }

        }
    }
}







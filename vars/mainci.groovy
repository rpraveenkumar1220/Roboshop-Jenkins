def call() {
    node('workstation') {

        stage('Code Checkout'){
            echo "Removing the previous code content"
            sh 'find . | grep "^./" |xargs rm -rf'

            if(env.TAG_NAME ==~ ".*") {
                env.gitbrname = "refs/tags/${env.TAG_NAME}"
            } else {
                env.gitbrname = "${env.BRANCH_NAME}"
            }
            checkout scm: [$class: 'GitSCM',
                           userRemoteConfigs: [[url: "https://github.com/rpraveenkumar1220/${env.component}"]],
                           branches: [[name: gitbrname]]],
                           poll: false

        }
        stage('Build') {
            if(env.cibuild == "nodejs" ) {
                sh 'npm install'
            }
            if( env.cibuild == "java" ){
                sh 'mvn  package'
            }
            else{
                echo "Since Component is ${env.component} No build is required "
            }

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
                if(env.cibuild=="java"){
                    sh 'mv /target/${component}-1.0.jar ${component}.jar'
                    sh 'rm -f pom.xml src target'
                }
                sh 'rm -rf Jenkinsfile'
                sh 'echo ${TAG_NAME} > VERSION'
                sh 'zip -r ${component}-${TAG_NAME}.zip *'
                sh 'curl -v -u admin:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.12.205:8081/repository/${component}/${component}-${TAG_NAME}.zip'
            }

        }
    }
}







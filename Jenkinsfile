pipeline{

    agent any

    environment{
        //Environment Variables Template
        //EnvVarName ="BLAH"

        // This is the ID of the credential name in Jenkins - set as SecretText
        // DockerCreds ="RevatureECR"

        // This is the tag applied to the Docker Image repository/projectname
        // DockerTagName ="walldiv/projectname"

        //The list of emails to send notifications to on failures - comma seperated
        EmailList ="walldiv@gmail.com"

        //Webhook URL - used for DiscordBot
        WebHookURL ="https://discordapp.com/api/webhooks/722541545651306528/Nd4lDlM0UMTo-vLZY76PxpDwmVrqr-osxp4k6ChLqAApb_e-Gps-IwDh8JpwZV4qCmZJ"
    }

    tools{
        maven 'M3'
    }
    
    //STAGES NAMES are agnostic - theyre symbolic to what phase you want to categorize it with no required name/keyword.
    stages{
        stage ("initializeDebug") {
            steps {
                sh '''
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        // stage('install'){
        //     steps{
        //         //fixes JDBC driver dependancies prior to packaging .jar
        //         // sh 'mvn install:install-file -Dfile="./src/main/resources/ojdbc7.jar" -DgroupId="com.oracle"  -DartifactId=ojdbc7 -Dversion="12.2.0.1" -Dpackaging=jar'
        //         // sh "mvn clean package -DskipTests=true"
        //     }
        // }

        // stage('Build the image'){
        //     steps{
        //         script{
        //             //param input is a tagname to be applied 
        //             // dockerImage = docker.build("${DockerTagName}:latest")
        //         }
        //     }
        // }

        // stage ('Deploy image to DockerHub'){
        //     steps{
        //         script{
        //             //if left blank - will push to docker.io
        //             // docker.withRegistry('', DockerCreds) {
        //             //     dockerImage.push()
        //             // }
        //         }
        //     }
        // }

        // stage ("Remove unUsed docker image"){
        //     steps{
        //         // sh "docker rmi ${DockerTagName}:latest"
        //     }
        // }
    }

    post {
        failure {
            discordSend description: "Jenkins Pipeline Build", footer: "Footer Text", link: env.BUILD_URL, result: currentBuild.currentResult, title: JOB_NAME, webhookURL: "https://discordapp.com/api/webhooks/722541545651306528/Nd4lDlM0UMTo-vLZY76PxpDwmVrqr-osxp4k6ChLqAApb_e-Gps-IwDh8JpwZV4qCmZJ"
            // emailext (
            //     subject: "FAILED: '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            //     body: "JOB '${env.JOB_NAME} [${env.BUILD_NUMBER}]' has failed on '${env.BUILD_TIMESTAMP}'.\nGIT URL: '${env.GIT_URL}'\nGIT BRANCH: '${env.GIT_BRANCH}'\nGIT COMMIT SHA: '${env.GIT_COMMIT}'\nCheck the console output at '${env.BUILD_URL}'.",
            //     to: '${EmailList}',
            //     attachLog: true
            // )
        }
    }

}

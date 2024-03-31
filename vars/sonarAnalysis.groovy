@NonCPS
def call(boolean abortPipeline = false) {
    withSonarQubeEnv('Sonar-Local') {
        '/opt/homebrew/Cellar/sonar-scanner/5.0.1.3006/bin/sonar-scanner'.execute()
    }

    try {
        timeout(time: 10, unit: 'MINUTES') {
            def qualityGateResult = waitForQualityGate()
            if (qualityGateResult.status != 'OK') {
                if (abortPipeline) {
                    error "Pipeline aborted due to quality gate failure: ${qualityGateResult.status}"
                }
                else {
                    echo "Quality gate failed: ${qualityGateResult.status}"
                }
            } else {
                echo "Quality gate passed: ${qualityGateResult.status}"
            }
        }
    }
    catch (e) {
        if (abortPipeline) {
            error 'Fallos encontrados en el Quality Gate.'
        }
    }
}

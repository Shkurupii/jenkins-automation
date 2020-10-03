import jenkins.automation.builders.PipelineJobBuilder

def pipelineScript = """
    pipeline {
        agent { label 'master' }
        parameters {
            string defaultValue: 'fuck off', description: '', name: 'foo', trim: true
        }
        stages {
            stage('hello') {
                steps {
                    sh 'echo "Hello World"'
                }
            }
        }
    }
"""

new PipelineJobBuilder(
        name: 'Job 1',
        description: 'This is a simple Job 1',
        pipelineScript: pipelineScript,
        sandboxFlag: true
).build(this).with {
    logRotator {
        numToKeep(5)
    }
}

new PipelineJobBuilder(
        name: 'Job 2',
        description: 'This is a simple Job 2',
        pipelineScript: pipelineScript,
        sandboxFlag: true
).build(this).with {
    logRotator {
        numToKeep(5)
    }
}

new PipelineJobBuilder(
        name: 'Pipeline builder with Job 1 and Job 2',
        description: 'this is a simple pipeline job',
        stages: [[
                         stageName : 'First stage',
                         jobName   : 'Job 1',
                         parameters: "[[\$class: 'StringParameterValue', name: 'foo', value: 'bar']]"
                 ],
                 [
                         stageName: 'Second stage',
                         jobName  : 'Job 2',
                 ]]
).build(this).with {
    logRotator {
        numToKeep(365)
    }
}

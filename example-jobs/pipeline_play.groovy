import jenkins.automation.builders.PipelineJobBuilder

def pipelineScript = """
#!groovy

def projectProperties = [
        [\$class: 'BuildDiscarderProperty', strategy: [\$class: 'LogRotator', numToKeepStr: '5']],
]

properties(projectProperties)

node() {
    stage('Clean workspace') {
        deleteDir()
        sh 'ls -lah'
    }
    stage('Checkout source') {
        git url: "https://github.com/Shkurupii/pipeline-dsl-seed.git",
                branch: 'master'
    }
    stage('Run playbook') {
        timeout(45) {
            sh '''#!/usr/bin/env bash
                cp -rf ${VAULT_PASSWORD_FILE} ${WORKSPACE}/.vault_password
                pip install mitogen --target . --no-deps
                export ANSIBLE_STRATEGY_PLUGINS=ansible_mitogen/plugins/strategy
                export ANSIBLE_STRATEGY=mitogen_linear
                export ANSIBLE_FORCE_COLOR=true
                export ANSIBLE_HOST_KEY_CHECKING=false

                ansible-playbook ${PLAYBOOK} -i inventory/${inventory} --limit ${LIMIT} --vault-id .vault_password ${USER_EXTRA}
                rm -f ${WORKSPACE}/.vault_password
                '''
        }
    }
}
"""


new PipelineJobBuilder(
        name: 'Hello Pipeline With Script',
        description: 'This is a simple pipeline job',
        pipelineScript: pipelineScript,
        sandboxFlag: true
).build(this).with {
    logRotator {
        numToKeep(5)
    }
}


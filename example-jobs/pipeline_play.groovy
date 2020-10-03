import jenkins.automation.builders.PipelineJobBuilder

def filePath = "playbook.groovy"
String pipelineScript(String filePath) {
    File file = new File(filePath)
    String utf8Content = file.getText("UTF-8")
    return utf8Content
}

new PipelineJobBuilder(
        name: 'Hello Pipeline With Script',
        description: 'This is a simple pipeline job',
        pipelineScript: pipelineScript(),
        sandboxFlag: true
).build(this).with {
    logRotator {
        numToKeep(5)
    }
}


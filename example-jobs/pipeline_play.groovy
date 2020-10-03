import jenkins.automation.builders.PipelineJobBuilder
String pipelineScript(String filePath) {
    File file = new File(filePath)
    String utf8Content = file.getText("UTF-8")
    return utf8Content
}

new PipelineJobBuilder(
        name: 'Hello Pipeline With Script',
        description: 'This is a simple pipeline job',
        pipelineScript: pipelineScript('playbook.groovy'),
        sandboxFlag: true
).build(this).with {
    logRotator {
        numToKeep(5)
    }
}


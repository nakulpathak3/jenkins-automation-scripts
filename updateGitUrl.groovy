import hudson.plugins.git.*

def modifyGitUrl(url) {
  // Your script here
  if (url[-8 .. -1] == "modified") {
  	return url - "modified"
  }
  else {
    return url
  }
}

Jenkins.instance.items.each {
  if (it.scm instanceof GitSCM) {
    def oldScm = it.scm
    def newUserRemoteConfigs = oldScm.userRemoteConfigs.collect {
      new UserRemoteConfig(modifyGitUrl(it.url), it.name, it.refspec, it.credentialsId)
    }
    println newUserRemoteConfigs
    def newScm = new GitSCM(newUserRemoteConfigs, oldScm.branches, oldScm.doGenerateSubmoduleConfigurations,
                            oldScm.submoduleCfg, oldScm.browser, oldScm.gitTool, oldScm.extensions)
    it.scm = newScm 
    it.save()
  }
}

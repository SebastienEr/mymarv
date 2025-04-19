folder('Tools') {
    description('Folder for miscellaneous tools.')
}

job('Tools/clone-repository') {
    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }
    steps {
        shell('git clone "$GIT_REPOSITORY_URL"')
    }
    wrappers {
        preBuildCleanup()
    }
}

job('Tools/SEED') {
    parameters {
        stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name')
        stringParam('DISPLAY_NAME', '', 'Display name for the job')
    }
    steps {
        shell('cp /var/jenkins_home/job_dsl.groovy .')
        dsl {
            external('job_dsl.groovy')
        }
    }
}

def githubName = System.getenv('GITHUB_NAME')
def displayName = System.getenv('DISPLAY_NAME')

if (githubName && displayName) {
    job(displayName) {
        scm {
            git("https://github.com/${githubName}.git")
        }
        properties {
            githubProjectUrl("https://github.com/${githubName}")
        }
        triggers {
            scm('* * * * *')
        }
        wrappers {
            preBuildCleanup()
        }
        steps {
            shell('make fclean')
            shell('make')
            shell('make tests_run')
            shell('make clean')
        }
    }
}

# ContinuousSecurity



## Demos

### Intial Jenkins Setup.

1. Docker-Compose up from the source directory.
2. Once it has finished starting, from another window run ```docker container exec -it $ID cat /var/jenkins_home/secrets/initialAdminPassword```
3. Copy/Paste the admin password.
4. Login to localhost:8080 using password above and accept defaults.
5. Install OWASP Dependency Check Plugin

### Maven Jenkins Setup
Prereq: Complete Initial Jenkins setup & start your docker containers.

1. ```docker exec -u root -it jenkins bash```
2. ```apt-get update && apt-get install -y maven```
3. exit out of shell
4. Create a new freestyle project called owaspmaven
5. Add source control git pointed to https://github.com/BillDinger/ContinuousSecurity
6. Check the box under 'build environment' that says delete the workspace before build starts.
   Under 'build' select 'execute shell command' and type in the box 'cd java'
7. Under 'build' select 'Invoke Top Level Maven Targets'
8. Expand the box and enter ```clean compile -P analysis```


### Maven Dependency Check Setup 

Prereq: Complete Initial Jenkins Setup & Maven Jenkins Setup

1. Go to http://localhost:8080/pluginManager/
2. Install OWASP Dependency-Check Plugin & restart jenkins
3. Go to your owaspmaven project and select add a build step and then dependency check
4. in the box for directories to scan type in ```/*java/*.jar```
5. Select Add Post Build Options & 'publish dependency-check results'
6. In the dependency check results enter ```**/dependency-check-report.xml```

### Maven OWASP Zap Setup.
Prereq: Complete initial jenkins setup & maven jenkins setup.

1.

### Owasp ZAP Standalone scan
1. docker run -i owasp/zap2docker-stable zap-cli quick-scan --self-contained --start-options "-config api.disablekey=true" http://exampe/url/to/hit
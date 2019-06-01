# ContinuousSecurity

Describes how to setup a local docker environment to use jenkins & owasp tooling for the talk. Demos below are organized around PHP (drupal), Maven, Jenkins, and IIS/.NET.

## Demos

1. Clone Repository
2. Docker-Compose up in directory.

## Jenkins Setup

### Jenkins Initial Setup.

1. Docker-Compose up from the source directory.
2. Once it has finished starting, from another window run ```docker container exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword```
3. Copy/Paste the admin password.
4. Login to localhost:8080 using password above and accept defaults.
4. Install recommended plugins.
5. Setup admin username/password.
6. Set to run on port:8080


### Jenkins Maven Setup
Prereq: Complete Initial Jenkins setup.

1. ```docker exec -u root -it jenkins bash```
2. ```apt-get update && apt-get install -y maven```
3. exit out of shell - type ```exit```
4. Go to jenkins in your browser http://localhost:8080/ 
5. Select new Item & then freestyle project called owaspmaven
6. Under 'source code management' select 'git' and set repository url to https://github.com/BillDinger/ContinuousSecurity
7. Check the box under 'build environment' that says delete the workspace before build starts.
8. Under 'build' select 'add build step' and then select 'Invoke Top Level Maven Targets'
9. Expand the box and enter ```clean compile -P analysis```
10. click 'advanced' and then under 'POM' enter 'java/pom.xml'
11. click save & then 'build now' , verify build is succesful.


### Jenkins Maven Dependency Check Plugin Setup 

Prereq: Complete Jenkins Setup & Jenkins Maven Setup

1. Go to http://localhost:8080/pluginManager/
2. Install OWASP Dependency-Check Plugin & restart jenkins
3. Go to your owaspmaven project (http://localhost:8080/job/owaspmaven/configure) and select add a build step and then dependency check
4. in the box for directories to scan type in ```/*java/*.jar```
5. Select Add Post Build Options & 'publish dependency-check results'
6. In the dependency check results enter ```**/dependency-check-report.xml```

### Jenkins PHP Dependency Check Setup
Prereq: Complete Jenkins Setup & maven setup (the CLI scanner requires a JRE)

1. Go to jenkins in your browser http://localhost:8080/ 
2. Select new Item & then freestyle project called owaspphp
3. Under 'source code management' select 'git' and set repository url to https://github.com/BillDinger/ContinuousSecurity
4. Check the box under 'build environment' that says delete the workspace before build starts.
5. Under 'build' select 'add build step' and then select 'Execute Shell'
6. Expand the box and enter ```${WORKSPACE}/cli/bin/dependency-check.sh  --project php  --out ${WORKSPACE} --scan ${WORKSPACE}/php -l ${WORKSPACE}/out.log --enableExperimental```
    This command will scan the directory `php`, output the log file & report in the root of the jenkins workspace. The
    `enableExperimental` flag is necessary 

### Jenkins OWASP Zap Setup.
Prereq: Complete initial jenkins setup & maven jenkins setup.

1. ```docker exec -u root -it jenkins bash```
2. ```apt-get update && apt-get install -y docker```
3. Go to jenkins in your browser http://localhost:8080/ 

### Owasp ZAP Standalone scan
1. docker run -i owasp/zap2docker-stable zap-cli quick-scan --self-contained --start-options "-config api.disablekey=true" http://exampe/url/to/hit

## Dotnet Setup
Note: the dotnet solution is a shim website design to just show how a scan can be done. It by default is designed to be
installed locally on port 80, although of course you can alter this if you choose.

### Dotnet Website setup.
1. Open up solution in visual studio.
2. Publish solution to \wwwroot\owasplocaldev
3. Create an IIS Site pointed to \wwwroot\owasplocaldev & create an application pool with 'no managed code'
4. Install URL Rewrite module into your IIS using web platform installer if you haven't alreayd.
5. https://download.visualstudio.microsoft.com/download/pr/5ee633f2-bf6d-49bd-8fb6-80c861c36d54/caa93641707e1fd5b8273ada22009246/dotnet-hosting-2.2.1-win.exe install dotnet hosting 2.2.1
6. Add a host file entry for localdev.owasp.com to 127.0.0.1
7 add IIS binding for localdev.owasp.com for port 80.

## PHP Setup

### PHP CLI Example

1. execute the following command `dependency-check.bat  --project test  --out {PATH_TO_REPO} --scan {PATH_TO_REPO}\php -l {PATH_TO_REPO}\out.log --enableExperimental` replacing the `{PATH_TO_REPO}` with the location where you cloned this repo.
2. the report can be viewed by opening the generated `dependency-check.html` in the root directory.

### Drupal Setup
1. Start drupal by doing `docker-compose -f drupal.yml up` 
2. browse to https://localhost:8083
3. select English
4. Select umami profile 
5. for Database options enter:
    Database type: PostgreSQL
    Database name: postgres
    Database username: postgres
    Database password: example
    ADVANCED OPTIONS; Database host: postgres
6. Enter whatever email, username/password you want.

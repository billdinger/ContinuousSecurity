# ContinuousSecurity

Describes how to setup a local docker environment to use jenkins & owasp tooling for the talk. Demos below are organized around the tools we're using. [Owasp Dependency Check](https://jeremylong.github.io/DependencyCheck/), [Owasp ZAP](https://github.com/zaproxy/zaproxy/wiki/Docker), [Owasp O-Saft](https://github.com/OWASP/O-Saft), and the Jenkins tooling around them.

## Owasp Dependency Check

### Java

0. Clone the repository.
1. Ensure you have [maven installed](http://maven.apache.org/download.cgi).
2. Go to the java directory - `cd java`
3. execute `mvn dependency-check:check`to just execute the OWASP check or a regular mvn build to see the failure later on. By default in the POM the maven check is running in the verify step.

### DotNet

0. Clone the repository.
1. You will need [dotnet core 3.1 installed](https://dotnet.microsoft.com/download/dotnet-core/3.1)
2. Go to the dotnet directory - `cd dotnet` & then the project directory `cd OwaspDemo`
3. Compile the project with `dotnet build OwaspDemo.sln`. Owasp Dependency Check will run after the compilation.

### PHP

0. Clone the repository.
1. `cd php`
2. execute the following command `../cli/bin/dependency-check.sh  --project owaspdemo  --out . --scan . --enableExperimental --data owaspdata --cveValidForHours 24 --failOnCVSS 4`
3. Can view the report by opening the dependency-check.html 

## Jenkins 

### Jenkins Initial Setup.

0. Clone the repository.
1. Docker-Compose up from the source directory.
2. Once it has finished starting, from another tab run ```docker container exec -it jenkinsowasp cat /var/jenkins_home/secrets/initialAdminPassword```
3. Copy/Paste the admin password.
4. Login to localhost:8080 using password above and accept defaults.
5. Install recommended plugins.
6. Setup admin username/password.
![admin setup](https://i.imgur.com/QrZbfhh.png)
7. Set to run on port:8080
8. You might need to restart jenkins. At which point you'll be able to login to [http://localhost:8080](http://localhost:8080)


### Jenkins Maven Setup
Prereq: Complete Initial Jenkins setup.

1. ```docker exec -u root -it jenkinsowasp bash```
2. ```apt-get update && apt-get install -y maven```
3. exit out of shell - type ```exit```
4. Go to jenkins in your browser [http://localhost:8080](http://localhost:8080/)
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
1. `docker run -i owasp/zap2docker-stable zap-cli quick-scan --self-contained --start-options "-config api.disablekey=true" http://exampe/url/to/hit`


### Drupal Setup
Just a standard drupal site for use in proof of concept testing the OWASP zap scanner.

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

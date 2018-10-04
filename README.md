# ContinuousSecurity



## Jenkins Demos

### Intial Jenkins Setup.

1. Docker-Compose up from the source directory.

2. Once it has finished starting, from another window run ```docker container exec -it $ID cat /var/jenkins_home/secrets/initialAdminPassword```

3. Copy/Paste the admin password.

4. Login to localhost:8080 using password above and accept defaults.

5. Install OWASP Dependency Check Plugin

### Maven Jenkins Setup
Prereq: Complete Initial Jenkins setup & start your docker containers.

1. docker exec -u root -it jenkins bash
2. apt-get update && apt-get install -y maven
1. apt-get update && apt-get install -y maven
1. Create a new freestyle project called owaspmaven
2. Add source control git pointed to https://github.com/BillDinger/ContinuousSecurity
3. Check the box under 'build environment' that says delete the workspace before build starts.
   Under 'build' select 'execute shell command' and type in the box 'cd java'
4. Under 'build' select 'Invoke Top Level Maven Targets'
5. Expand the box and enter ```
  clean
  compile
  -P analysis```



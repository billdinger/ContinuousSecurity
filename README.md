# ContinuousSecurity



# Intial Jenkins Setup.

1.) Docker-Compose up from the source directory.

2.) Once it has finished starting, from another window run ```docker container exec -it $ID cat /var/jenkins_home/secrets/initialAdminPassword```

3.) Copy/Paste the admin password.

4.) Login to localhost:8080 using password above and accept defaults.


# Setup a freestyle build.
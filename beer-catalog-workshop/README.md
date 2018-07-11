# ACME Beer Catalog Lab Setup

## Prerequisites

* Each attendee / group of attendees should have a laptop running Lunix / Mac OS X or having access to a Linux terminal
* Internet access with no blacklist filtering on `*.apicurio.io`, `*.github.com`, `*.githubusercontent.com`, `*.getpostman.com`
* Ability to install Postman client tool on laptop
* Ability to install OpenShift client tool (`oc`) on laptop or remote machine
* Ability to install system utilities (`curl`, `git`, `sed`, `jq`) on laptop or remote machine
* An OpenShift cluster with enough resources for instantiating ~15 pods using 3 GB RAM per attendee / group of attendees

## System utilities setup

### On Linux
Depending on your distribution, you should be able to install `jq` through your package manager.

On Ubuntu / Debian : `sudo apt-get install jq`
On Fedora : `sudo dnf install jq`
On RHEL/CentOS :
```sh
$ yum install epel-release -y
$ yum install jq -y
```

On other distros, as jq has no runtime dependencies, you may just use :
```sh
$ wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.5/jq-linux64
$ chmod +x ./jq
$ cp jq /usr/bin
```

### On Mac OS X

The easiest path is to install missing parts using Homebrew. If Homebrew not present on your laptop, just run the following command :
```sh
$ usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

Then, when Homebrew is setup, just run this command to install jq (everything else should just be already there) :
```sh
$ brew install jq
```

### Pre-Lab Setup

## Ansible Tower setup

In a transversal project that may be accessible to each attendee / group of attendees, instantiate an Ansible Tower. This setup should be done before the lab. It will require ansible command line (`brew install ansible` or `yum install ansible`)

### 1/ Prepare a Persistent Volume Claim

Once logged in your target projet:

```sh
$ oc apply -f - <<EOF
apiVersion: "v1"
kind: "PersistentVolumeClaim"
metadata:
  name: "postgresql"
spec:
  accessModes:
    - "ReadWriteOnce"
  resources:
    requests:
      storage: "5Gi"
EOF
```

### 2/ Clone the Git repository for community AWX and logos

```sh
$ git clone -b 1.0.6 https://github.com/ansible/awx.git
$ git clone https://github.com/ansible/awx-logos.git
```

### 3/ Run the install playbook

Provide `<project>`, `<master_host>` (without https:// pragma), `<openshift_admin>`, `<openshift_admin_pwd>` as well as AWX admin and password :

```sh
$ cd awx/installer/
$ ansible-playbook -i inventory install.yml -e dockerhub_version=1.0.6 \
   -e openshift_host=<master_host> -e openshift_skip_tls_verify=true \
   -e openshift_project=<project> -e openshift_user=<openshift_admin> \
   -e openshift_password=<openshift_admin_pwd> -e default_admin_user=admin \
   -e default_admin_password=redhat123 -e awx_official=true
```

Example on a local Red Hat Container Development Kit (CDK), deploying into project named `fabric`:

```sh
$ ansible-playbook -i inventory install.yml -e dockerhub_version=1.0.6 \
    -e openshift_host=192.168.99.100:8443 -e openshift_skip_tls_verify=true \
    -e openshift_project=fabric -e openshift_user=admin \
    -e openshift_password=admin -e default_admin_user=admin \
    -e default_admin_password=redhat123 -e awx_official=true
```

Wait for deployment to be finished.

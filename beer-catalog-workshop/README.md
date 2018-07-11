# ACME Beer Catalog Workshop

This repository contains materials and instructions on how to run a Workshop around the `beer-catalog-demo` introduced on Red Hat Developers Blog into a 3 part blog series called [An API Journey: from Mock to Deployment the Agile way](https://developers.redhat.com/blog/2018/04/11/api-journey-idea-deployment-agile-part1/).

While blog series was focusing of giving a broad view of the API Full Lifecycle, we have found many customers asking for detailed workshop on that topic. Especially regarding the continuous delivery of API Configuration within the 3scale API Management backend.

## Overview

During this workshop, you will go through the different stages composing the API Full Lifecycle and you will learn how to craft an API, mock it to break dependencies, built it using contract-first approach and deploy your API implementation and policy configuration in a continuous way. You'll also see how to use 3scale API Management capabilities to monitor and monitize your API.

The workshop setup is as shown in schema below. We'll use:
  * One or many `INFRA` environment to host the components used during building and deploying. Namely Apicurio, Microcks, Jenkins and Ansible Tower. See the section on *Workshop variants* for discussion on precise setup,
  * One `DEV` environment where attendees will deploy their API implementation (based on Spring Boot but this is also subject to variants),
  * One `TEST` environment where API implementation will be deployed once tested alongside with an API Gateway. In `TEST` environment, we primarily focus on testing/validating the API Gateway policies and configuration as well as feeding API Management system with API declaration,
  * One `PROD` environment where API implementation and API Gateway configuration are promoted after having tested. On the API Management backend side, API definition should also appear as deployed onto a Production environment.

![Workshop setup](./assets/workshop-setup.png)

The workshop also implies showing how:
  * Container platforms like OpenShift/Kubernetes allows easy promotion of container based development and API Gareways from environment to environment,
  * OpenAPI Specification can be used as a central asset and a unique *source of truth* for each and every deployment,
  * 3scale API Management backend can be continuously updated and enriched with new API or new API version deployment.

## Materials

  You'll find in this repository:
  * [Setup instructions](./SETUP.md) with pre-requisites and infrastructure,
  * [Running instructions](./RUNNING.md) with instructions on how to run, what actions/commands execute depending on the deployment variant chosen for this workshop.

## Variants

The described workshop is always using:
  * Hosted version of [Apicurio](https://studio.apicur.io/) because it's really straightforward to create an account,
  * Hosted version of [3scale API Management backend](https//www.3scale.net) because the setup of a dedicated instance may be a hard task. You may have to request evaluation accounts if you want the attendees to be autonomous.

Depending on your time and resources, you may want to use different variants of this workshop. We have tried below to provide the most detailed assets so that you will be able to tune the workshop to best fit your attendees expectations.

### OpenShift

The Workshop can be run on a **mutualized** OpenShift cluster or either on a **local** Red Hat Container Developer Kit (CDK) or Minishift instance. In the case of a workshop running on each attendee laptop, each of us will have to go through the [detailed setup](./SETUP.md).

### Microcks + Jenkins

As Microcks is not made to be multitenant, you may come into issue if you want to make every attendees work with the same instance. Making them import the same Postman Collection many time will result into collision. So if you want to put the emphasis on this part, be sure to prepare a set of Postman Collection with different API names and version numbers.

If you plan to run a full 2 days workshop, you may find valuable to make every attendee / group of attendee replay the Microcks and Jenkins sections of the [detailed setup](./SETUP.md).

### Pre-population vs step-by-step approach

Some of us want to prepare everything and some of us want to do things step-by-step. Depending on the emphasis you may put, we'll give your different options. If you want to prepare the most of things, you may want to create each projects for each of your provided users that way:

```sh
$ for user in user01 \
 user02 \
 user03 \
 user04 \
 user05 \
 user06 \
 user07 \
 user08 \
 user09 \
 user10;
 do
 oc new-project ${user}-beer-catalog-dev --display-name="Beer Catalog (DEV) - ${user}"
 oc new-project ${user}-beer-catalog-test --display-name="Beer Catalog (TEST) - ${user}"
 oc new-project ${user}-beer-catalog-prod --display-name="Beer Catalog (PROD) - ${user}"
 oc adm policy add-role-to-user admin ${user} -n ${user}-beer-catalog-dev
 oc adm policy add-role-to-user admin ${user} -n ${user}-beer-catalog-test
 oc adm policy add-role-to-user admin ${user} -n ${user}-beer-catalog-prod
 done
```

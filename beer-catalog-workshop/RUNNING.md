# Running ACME Beer Catalog Workshop

Throughout this workshop, we are discussing the API Full Lifecycle as shown below.

![API Full Lifecycle](./assets/api-full-lifecycle.png)

We are going to delve and detail each and every stage on the ACME Beer Catalog use-case that is introduces and exposed into [An API Journey: from Mock to Deployment the Agile way](https://developers.redhat.com/blog/2018/04/11/api-journey-idea-deployment-agile-part1/) blog series.

In this document, we'll only cover the stages that require technical materials such as contracts, scripts, templates and so on...

## Stage 2: Design

This stage is an opportunity to have discussion around: contract-first approach, semantic versioning best practices, data modeling, collaboration around APIcurio and Git, Swagger and OpenAPI specifications, ...

The API Definition you may want to import in APIcurio : https://raw.githubusercontent.com/microcks/api-lifecycle/master/beer-catalog-demo/api-contracts/beer-catalog-api-swagger.json

> If you plan to use the same Microcks instance for attendees and let them recreate the mocks, ensure to make them create different variants of this API by changing the name or the version. Also, make them pus their work to different Git repositories.

## Stage 3: Mock

This stage requires having Postman installed somewhere on attendees laptop.

It is the appropriate time to discuss: utility of mocking, ways to provide representative samples for an API, the documentation information it represents, how to make the dataset evolve and be enriched throughout the whole life of API, the matching of dataset with API versioning, ...

Many ways of doing things:
* Load this reference Postman Collection in Postman : https://raw.githubusercontent.com/microcks/api-lifecycle/master/beer-catalog-demo/api-contracts/Beer%20Catalog%20API.postman_collection.json and just explore it than create a Job within Microcks with the same URL.
* Let attendees clone the reference and import it in their Postman instance, change API name and version if you plan to use the same Microcks instance,
* Import the API contract designed on **Stage 2** and make attendees create their samples step by step.

> If you choose one of the two latest options, be really careful about the respect of conventions regarding version property in Postman to allow smooth import into Microcks later.

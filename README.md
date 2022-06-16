<h1 align="center"> Email Sender </h1> <br>

<p align="center">
  A service to send emails.
</p>


## Table of Contents

- [Features](#quick-start)
- [Requirements](#requirements)
- [Testing](#testing)
- [TODO](#todo)


## Features
Single REST endpoint that sends email through a mail server using there API.
* Mailgun as the main mail provider, secondary provider is mailjet.
* [API Docs](http://email-service-siteminder.osc-fr1.scalingo.io/swagger-ui/index.html?configUrl=/api-docs/swagger-config#/email-sender-rest-controller/sendEmail)

## Requirements
* [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi)
* [Lombok](https://projectlombok.org)

## TODO
* Batch sending of emails
* JMS Endpoint





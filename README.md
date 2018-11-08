# OAuth2 Plugin for Graylog

[![Build Status](https://travis-ci.org/globocom/graylog-plugin-oauth2.svg?branch=master)](https://travis-ci.org/globocom/graylog-plugin-oauth2)

This plugins adds Oauth2 capabilities to Graylog. It supports automatic login and user account creation based on Oauth 2.0.

**Required Graylog version:** 2.4 and later

Installation
------------

[Download the plugin](https://github.com/none/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Development
-----------

If you are developing the plugin with graylog-server, you should follow these steps:



* import the module into graylog-project pom.xml

       
       <module>../graylog-project-repos/graylog-plugin-oauth2</module>
         
        

* import the jar into graylog-project pom.xml

            
        <dependency>
                   <groupId>com.globo</groupId>
                    <artifactId>graylog-plugin-oauth2</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
        </dependency>
Usage
-----
   
You need to enter the authentication page and configure your Oauth plugin with all the required fields.

Works only with Authentication code.

Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.


Join Us
------------

If you want to make part of the Globo.com Team, check out our [Open positions](https://talentos.globo.com/#/oportunidades).

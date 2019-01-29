# OAuth2 Plugin for Graylog

[![Build Status](https://travis-ci.org/globocom/graylog-plugin-oauth2.svg?branch=master)](https://travis-ci.org/globocom/graylog-plugin-oauth2) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/cb9d46aefdbb40a7a026b9156ab8db21)](https://www.codacy.com/app/igorcavalcante/graylog-plugin-oauth2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=globocom/graylog-plugin-oauth2&amp;utm_campaign=Badge_Grade)

This plugins adds Oauth2 capabilities to Graylog. It supports automatic login and user account creation based on Oauth 2.0. 

***Unfortunately, it works only with an authorization code and so far it also needs a Nginx in front of Graylog server to work properly.***

**Required Graylog version:** 2.5 and later

Version Compatibility
---------------------

    | Plugin Version | Graylog Version |
    | -------------- | --------------- |
    | 2.5.x          | 2.5.x           |
    | 2.4.x          | 2.4.x           |
    | 2.3.x          | 2.3.x           |

## Installation

[Download the plugin](https://github.com/globocom/graylog-plugin-oauth2/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

## Development

If you are developing the plugin with graylog-server, you should follow these steps:

  * git clone https://github.com/Graylog2/gralog2-server.git
  * cd graylog2-server/graylog2-web-interface
  * ln -s $YOURPLUGIN plugin/
  * npm install && npm start


* import the module into graylog-project pom.xml

       <module>../graylog-project-repos/graylog-plugin-oauth2</module>

* import the jar into graylog-project pom.xml

           <dependency>
             <groupId>com.globo</groupId>
               <artifactId>graylog-plugin-oauth2</artifactId>
               <version>0.0.3-SNAPSHOT</version>
           </dependency>

## Usage

You must fill in the required OAuth configuration fields and has group mapping functionality, if you want to filter the roles by group you need to add in the group mapping screen.

* configure the nginx

        location / {
         if ($check_authgraylog = nook_auth) {
           return 302 https://url/authorize?response_type=code&redirect_uri=https://$server_name$request_uri&client_id=define;
         }
         proxy_set_header Host $http_host;
         proxy_set_header X-Forwarded-Host $host;
         proxy_set_header X-Forwarded-Server $host;
         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
         proxy_set_header X-Graylog-Server-URL https://$server_name/api;
         proxy_connect_timeout 60;
         proxy_send_timeout    60;
         proxy_read_timeout    60;
         send_timeout          60;
         proxy_pass       http://graylog-server;
        }
        
        location ~ ^/(api|assets) {
         proxy_set_header Host $http_host;
         proxy_set_header X-Forwarded-Host $host;
         proxy_set_header X-Forwarded-Server $host;
         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
         proxy_set_header X-Graylog-Server-URL https://$server_name/api;
         proxy_connect_timeout 60;
         proxy_send_timeout    60;
         proxy_read_timeout    60;
         send_timeout          60;
         proxy_pass       http://graylog-server;
        }
        
        location = / {
         return 302 https:/url/authorize?response_type=code&redirect_uri=https://$server_name/streams&client_id=define;
        }
        
        upstream graylog-server {
           server      0.0.0.0:9000;
           keepalive   90;
        }


## Getting started

This project is using Maven 3 and requires Java 8 or higher.

  * Clone this repository.
  * Run `mvn package` to build a JAR file.
  * Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
  * Copy generated JAR file in target directory to your Graylog plugin directory.
  * Restart the Graylog.

## Join Us

If you want to make part of the Globo.com Team, check out our [Open positions](https://talentos.globo.com/#/oportunidades).

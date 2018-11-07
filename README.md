# GloboAuth Plugin for Graylog


__Use this paragraph to enter a description of your plugin.__

**Required Graylog version:** 2.3 and later

Installation
------------

[Download the plugin](https://github.com/none/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Development
-----------

You can improve your development experience for the web interface part of your plugin
dramatically by making use of hot reloading. To do this, do the following:

* `importar o module e o jar no pom do graylog-project`

        <dependency>
            <groupId>com.globo</groupId>
            <artifactId>graylog-plugin-globo-auth</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>


Usage
-----

__Use this paragraph to document the usage of your plugin__


Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.


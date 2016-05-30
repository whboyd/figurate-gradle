## Gradle Keytool Plugin

# Requirements

In order to run the Keytool plugin you must ensure your JAVA_HOME environment variable is set to a JDK location, or you must specify a javaHome like so:

    keytool {
        javaHome = '/path/to/my/jdk/'
    }

# Common Keytool Commands:

* Export certificate to ASCII format:

    - keytool -exportcert -keystore <keystore> -alias <alias> -file <output_file> -rfc
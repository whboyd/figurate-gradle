package org.figurate.gradle.plugin.keytool

import org.gradle.api.tasks.Exec

import java.security.KeyStore

/**
 * Created by fortuna on 6/05/14.
 */
class KeytoolTask extends Exec {

    KeytoolTask() {
        def javaHome = project.keytool.javaHome ? project.keytool.javaHome : environment['JAVA_HOME']
        executable "${new File((String) javaHome, 'bin').canonicalPath}/keytool"
        extensions.add('options', new KeytoolArgsExtension())
    }

    def extractPrivateKey = { keystoreName, keystorePassword, alias ->
        KeyStore ks = KeyStore.getInstance('jks');
        ks.load(new FileInputStream(keystoreName), keystorePassword.toCharArray());
        StringBuilder builder = new StringBuilder()
        builder << "-----BEGIN PRIVATE KEY-----\n"
        builder << new sun.misc.BASE64Encoder().encode(ks.getKey(alias, keystorePassword.toCharArray()).getEncoded())
        builder << "-----END PRIVATE KEY-----\n"
        builder.toString()
    }

    class KeytoolArgsExtension {

        def argMap = [:]

        def propertyMissing(String name, String value) {
            if (value) {
                // allow dollar prefix to work around reserved keywords (e.g. 'new')
                String argKey = "-${name - ~/^\$/}"
                if (argMap[name]) {
                    def newArgs = KeytoolTask.this.args
                    newArgs[newArgs.indexOf(argKey) + 1] = value
                    KeytoolTask.this.setArgs newArgs
                } else {
                    KeytoolTask.this.args argKey, value
                }
                argMap[name] = value
            }
        }

        void setKeystore(String keystore) {
            KeytoolTask.this.args '-keystore', new File(keystore).canonicalPath
        }
    }
}

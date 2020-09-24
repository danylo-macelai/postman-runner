package co.poynt.postman;

import co.poynt.postman.dynamic.PostmanDynamicParameters;
import co.poynt.postman.runner.PostmanCollectionRunner;
import co.poynt.postman.testrail.PostmanTestrailSyncer;
import picocli.CommandLine;

@CommandLine.Command(name = "postman-tools", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
//@formatter:off
        subcommands = {
                PostmanCollectionRunner.class,
                PostmanDynamicParameters.class,
                PostmanTestrailSyncer.class
//@formatteer:on
        })
public class PostmanRunnerTools implements Runnable {

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}
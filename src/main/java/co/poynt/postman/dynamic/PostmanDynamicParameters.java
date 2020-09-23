package co.poynt.postman.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.poynt.postman.runner.PostmanCollectionRunner;
import picocli.CommandLine;

@CommandLine.Command(name = "dynamic", description = "Postman runner with dynamic parameters")
public class PostmanDynamicParameters extends PostmanCollectionRunner {

    private static final Logger logger = LoggerFactory.getLogger(PostmanDynamicParameters.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        initialize();

    }

    /**
     * FIXME: Document this type
     *
     * @apiNote $
     *
     */
    private void initialize() {

        logger.info("PostmanDynamicParameters.initialize()");

    }

}
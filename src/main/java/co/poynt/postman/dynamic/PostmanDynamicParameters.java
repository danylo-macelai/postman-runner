package co.poynt.postman.dynamic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import co.poynt.postman.model.PostmanUrlEncoded;
import co.poynt.postman.runner.PostmanCollectionRunner;
import picocli.CommandLine;

@CommandLine.Command(name = "dynamic", description = "Postman runner with dynamic parameters")
public class PostmanDynamicParameters extends PostmanCollectionRunner {

    private static final Logger logger = LoggerFactory.getLogger(PostmanDynamicParameters.class);

    @CommandLine.Option(names = { "-v",
            "--csv" }, required = true, description = "")
    private String csvFile;

    private Map<String, LinkedList<List<PostmanUrlEncoded>>> csvData;

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        initialize();

    }

    private void initialize() {
        csvData = new HashMap<>();
        File file = new File(csvFile);
        if (!file.exists()) {
            throw new RuntimeException(String.format("File [%s] is not found!", csvFile));
        }
        List<PostmanUrlEncoded> parameters;
        CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(true).build();
        try (FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                Reader reader = new BufferedReader(isr);
                CSVReader csv = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {

            String[] records;
            String[] headers = csv.readNext();
            while ((records = csv.readNext()) != null) {
                parameters = new ArrayList<>();
                for (int i = 1; i < records.length; i++) {
                    PostmanUrlEncoded param = new PostmanUrlEncoded();
                    param.key = headers[i];
                    if (!FilenameUtils.getExtension(records[i]).isEmpty() && Files.exists(Paths.get(records[i]))) {
                        param.src = records[i];
                        param.type = "file";
                    } else {
                        param.value = records[i];
                        param.type = "text";
                    }
                    parameters.add(param);
                }
                LinkedList<List<PostmanUrlEncoded>> items = csvData.get(records[0]);
                if (items == null) {
                    items = new LinkedList<>();
                }
                items.add(parameters);
                csvData.put(records[0], items);
            }
        } catch (Exception e) {
            logger.error("Failed to reader csv.", e);
        }
    }

}
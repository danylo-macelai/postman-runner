package co.poynt.postman.runner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import co.poynt.postman.model.PostmanUrlEncoded;

public class PostmanRunResult {
    public int totalRequest  = 0;
    public int failedRequest = 0;
    public int totalTest     = 0;
    public int failedTest    = 0;

    public List<String> failedRequestName = new ArrayList<>();
    public List<String> failedTestName    = new ArrayList<>();

    public String         fileLog;
    private StringBuilder logger          = new StringBuilder();
    private StringBuilder details         = new StringBuilder();
    private List<String>  failedIdRequest = new ArrayList<>();

    @Override
    public String toString() {
        loggerln("\n", "*************************************");
        loggerln("Total Requests = ", totalRequest);
        loggerln("Failed Requests = ", failedRequest);
        loggerln("Total Tests = ", totalTest);
        loggerln("Failed Tests = ", failedTest);
        loggerln("Failed Request ID: ", failedIdRequest);
        loggerln("Failed Request Names: ", failedRequestName);
        loggerln("Failed Test Names: ", failedTestName);
        loggerln("*************************************");

        if (fileLog != null) {
            saveLog();
        }

        return logger.toString();
    }

    public boolean isSuccessful() {
        return failedRequest == 0 && failedTest == 0;
    }

    public void loggerStartFolder(String folder) {
        loggerln("\n", "\n", "==============================================================");
        loggerln("POSTMAN Folder: ", folder);
    }

    public void loggerStartRequest(String fItem) {
        loggerln("\n", "===============================");
        loggerln("POSTMAN request: ", fItem);
        loggerln();
    }

    public void loggerTotalRequest() {
        totalRequest++;
        loggerln("==============");
        loggerln("ID: ", totalRequest);
        loggerln();
    }

    public void loggerParams(List<PostmanUrlEncoded> params) {
        if (params != null && !params.isEmpty()) {
            loggerln("Parameters: ");
            params.forEach(p -> loggertab(p.toString()));
            loggerln();
        }
    }

    public void loggerRequestId(String requestId) {
        loggerln("requestId: ");
        loggertab(requestId);
    }

    public void loggerTestSuccess() {
        if (details.length() > 0) {
            loggerln();
            loggerln("Test Success:");
            loggerln(details);
            details = new StringBuilder();
        }
    }
    public void loggerTestDetails(Object... info) {
        details.append("\t");
        Stream.of(info).forEach(i -> {
            details.append(i);
        });
        details.append("\n\n");
    }
    
    public void loggerFailedTest(String label, String test, PostmanHttpResponse response) {
        loggerln();
        loggerln(label);
        loggertab("Test:");
        loggertab(test);
        loggerln();
        if (details.length() > 0) {
            loggertab("Details:");
            loggerln(details);
            details = new StringBuilder();
        }
        loggertab("Response:");
        loggertab("Response Code: ", String.valueOf(response.code));
        loggertab(response.body);
        loggerln();

        failedIdRequest.add(String.valueOf(totalRequest));
    }

    private void loggerln(Object... info) {
        Stream.of(info).forEach(i -> {
            logger.append(i);
        });
        logger.append("\n");
    }

    private void loggertab(Object... info) {
        logger.append("\t");
        loggerln(info);
    }

    private void saveLog() {
        try {
            File targetFile = new File(fileLog);
            targetFile.createNewFile();
            try (Writer targetFileWriter = new FileWriter(targetFile)) {
                targetFileWriter.write(logger.toString());
                targetFileWriter.close();
            }
        } catch (IOException e) {
        }
    }

}

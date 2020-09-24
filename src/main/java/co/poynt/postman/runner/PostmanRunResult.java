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
    private StringBuilder sb              = new StringBuilder();
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

        return sb.toString();
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

    public void loggerFailedTest(String label, String test, PostmanHttpResponse response) {
        loggerln();
        loggerln(label);
        loggertab("a) Test");
        loggertab(test);
        loggerln();
        loggertab("b) Response");
        loggertab("Response Code: ", String.valueOf(response.code));
        loggertab(response.body);
        loggerln();

        failedIdRequest.add(String.valueOf(totalRequest));
    }

    private void loggerln(Object... info) {
        Stream.of(info).forEach(i -> {
            sb.append(i);
        });
        sb.append("\n");
    }

    private void loggertab(Object... info) {
        sb.append("\t");
        loggerln(info);
    }

    
    /**
     *  Creating a report of the work done
     *  
     *  Example:
     * 
     *            ==============================================================
     *            POSTMAN Folder: smartsheet
     *
     *            ===============================
     *            POSTMAN request: Auth
     *
     *            Parameters: 
     *                [key=login,src=<null>,type=text,value=admin]
     *                [key=password,src=<null>,type=text,value=123456]
     *
     *            requestId: 
     *                93aa95f5-a781-4b78-9e5f-0874b344766e
     *
     *            ===============================
     *            POSTMAN request: Cadastro
     *
     *            ==============
     *            ID: 2
     *
     *            Parameters: 
     *                [key=identificador,src=<null>,type=text,value=00002/2020]
     *                [key=codigo_tipo,src=<null>,type=text,value=10]
     *                [key=descritivo,src=<null>,type=text,value=Joao]
     *                [key=bin_imagem,src=C:\imgs\profile_22.jpg,type=file,value=<null>]
     *
     *            requestId: 
     *                c58d69cc-3d98-4e32-ab80-f40e38067012
     *            ==============
     *            ID: 3
     *
     *            Parameters: 
     *                [key=identificador,src=<null>,type=text,value=00001/2020]
     *                [key=codigo_tipo,src=<null>,type=text,value=10]
     *                [key=descritivo,src=<null>,type=text,value=Maria]
     *                [key=bin_imagem,src=C:\imgs\profile_1.jpg,type=file,value=<null>]
     *
     *            requestId: 
     *                4ae6effa-5fe2-47b3-a96a-4d29290be17f
     *
     *            THERE ARE TEST FAILURES:
     *                a) Test
     *                const responseJson = JSON.parse(responseBody);
     *                
     *                tests["is success"] = responseJson.message.type == "success";
     *
     *                b) Response
     *                Response Code: 200
     *                {"data":{},"service":{"name":"Cadastro"},"message":{"type":"error","value":"Já existe registro com este identificador."}}
     *
     *            *************************************
     *            Total Requests = 3
     *            Failed Requests = 1
     *            Total Tests = 3
     *            Failed Tests = 1
     *            Failed Request ID: [3]
     *            Failed Request Names: [smartsheet.Cadastro]
     *            Failed Test Names: [Cadastro.is success]
     *            *************************************    
     */
    private void saveLog() {
        try {
            File targetFile = new File(fileLog);
            targetFile.createNewFile();
            try (Writer targetFileWriter = new FileWriter(targetFile)) {
                targetFileWriter.write(sb.toString());
                targetFileWriter.close();
            }
        } catch (IOException e) {
        }
    }

}

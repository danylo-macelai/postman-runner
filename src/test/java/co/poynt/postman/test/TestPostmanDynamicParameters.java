package co.poynt.postman.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.net.URL;

import org.testng.annotations.Test;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import co.poynt.postman.dynamic.PostmanDynamicParameters;

public class TestPostmanDynamicParameters  {

    /**
     *  Creating a report of the work done
     *  
     * 
     *  ==============================================================
     *  POSTMAN Folder: demo-dyna-params
     *
     *  ===============================
     *  POSTMAN request: Auth
     *
     *  Parameters: 
     *      [key=login,src=<null>,type=text,value=admin]
     *      [key=password,src=<null>,type=text,value=12345678]
     *
     *  requestId: 
     *      9bc8411a-6950-42a0-ab5e-7035100314e0
     *
     *  Test Success:
     *      tests["Status code is 200] = true
     *
     *      tests["Login is admin] = true
     *
     *      tests["Password is 12345678] = true
     *
     *
     *
     *  ===============================
     *  POSTMAN request: Cadastro
     *
     *  ==============
     *  ID: 2
     *
     *  Parameters: 
     *      [key=identificador,src=<null>,type=text,value=101]
     *      [key=codigo_tipo,src=<null>,type=text,value=10]
     *      [key=descritivo,src=<null>,type=text,value=AAA]
     *      [key=bin_imagem,src=<null>,type=text,value=C:\....\PostmanDynamicParameters.jpg]
     *
     *  requestId: 
     *      4d7b9e68-ee49-4790-9c81-c834e893dac9
     *
     *  Test Success:
     *      tests["Status code is 200] = true
     *
     *      tests["Token is ABCD12345] = true
     *
     *      tests["identificador is 101 or 102 or 103] = true
     *
     *      tests["codigo_tipo is 10 or 11 or 12] = true
     *
     *      tests["descritivo is AAA or BBB or CCC] = true
     *
     *
     *  ==============
     *  ID: 3
     *
     *  Parameters: 
     *      [key=identificador,src=<null>,type=text,value=102]
     *      [key=codigo_tipo,src=<null>,type=text,value=11]
     *      [key=descritivo,src=<null>,type=text,value=BBB]
     *      [key=bin_imagem,src=<null>,type=text,value=C:\....\PostmanDynamicParameters.jpg]
     *
     *  requestId: 
     *      765981e8-aa08-4466-be37-20e9b5a541e8
     *
     *  Test Success:
     *      tests["Status code is 200] = true
     *
     *      tests["Token is ABCD12345] = true
     *
     *      tests["identificador is 101 or 102 or 103] = true
     *
     *      tests["codigo_tipo is 10 or 11 or 12] = true
     *
     *      tests["descritivo is AAA or BBB or CCC] = true
     *
     *
     *  ==============
     *  ID: 4
     *
     *  Parameters: 
     *      [key=identificador,src=<null>,type=text,value=103]
     *      [key=codigo_tipo,src=<null>,type=text,value=12]
     *      [key=descritivo,src=<null>,type=text,value=CCC]
     *      [key=bin_imagem,src=<null>,type=text,value=C:\....\PostmanDynamicParameters.jpg]
     *
     *  requestId: 
     *      c0fac4a8-95b4-4e38-a1ef-321c8873e203
     *
     *  Test Success:
     *      tests["Status code is 200] = true
     *
     *      tests["Token is ABCD12345] = true
     *
     *      tests["identificador is 101 or 102 or 103] = true
     *
     *      tests["codigo_tipo is 10 or 11 or 12] = true
     *
     *      tests["descritivo is AAA or BBB or CCC] = true
     *
     *
     *
     *  *************************************
     *  Total Requests = 4
     *  Failed Requests = 0
     *  Total Tests = 18
     *  Failed Tests = 0
     *  Failed Request ID: []
     *  Failed Request Names: []
     *  Failed Test Names: []
     *  *************************************
     */
    @Test(enabled = true)
    public void testRunDynamic() throws Exception {
        URL resource = null;
        PostmanDynamicParameters dynamic = new PostmanDynamicParameters();

        resource = Thread.currentThread().getContextClassLoader()
                .getResource("PostmanDynamicParameters.postman_collection.json");
        Field colFilenameField = dynamic.getClass().getSuperclass().getDeclaredField("colFilename");
        colFilenameField.setAccessible(true);
        colFilenameField.set(dynamic, resource.getFile());

        resource = Thread.currentThread().getContextClassLoader()
                .getResource("PostmanDynamicParameters.postman_environment.json");
        Field envFilenameField = dynamic.getClass().getSuperclass().getDeclaredField("envFilename");
        envFilenameField.setAccessible(true);
        envFilenameField.set(dynamic, resource.getFile());

        Field folderNameField = dynamic.getClass().getSuperclass().getDeclaredField("folderName");
        folderNameField.setAccessible(true);
        folderNameField.set(dynamic, "demo-dyna-params");

        Field haltOnErrorField = dynamic.getClass().getSuperclass().getDeclaredField("haltOnError");
        haltOnErrorField.setAccessible(true);
        haltOnErrorField.set(dynamic, false);

        resource = Thread.currentThread().getContextClassLoader().getResource("PostmanDynamicParameters.csv");
        File original = new File(resource.getFile());
        String directory = original.getParent();
        File csvFile = new File(directory + File.separator + "_PostmanDynamicParameters.csv");
        try (FileReader fr = new FileReader(original);
                CSVReader reader = new CSVReader(fr);
                FileWriter fw = new FileWriter(csvFile);
                CSVWriter writer = new CSVWriter(fw)) {
            writer.writeNext(reader.readNext());
            String[] row;
            while ((row = reader.readNext()) != null) {
                row[4] = directory + File.separator + "PostmanDynamicParameters.jpg";
                writer.writeNext(row);
            }
            Field csvFileField = dynamic.getClass().getDeclaredField("csvFile");
            csvFileField.setAccessible(true);
            csvFileField.set(dynamic, csvFile.getPath());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        dynamic.run();
    }
}

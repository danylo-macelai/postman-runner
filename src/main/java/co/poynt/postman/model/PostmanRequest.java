package co.poynt.postman.model;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import co.poynt.postman.runner.HaltTestFolderException;

public class PostmanRequest {
    public String method;
    public List<PostmanHeader> header;
    public PostmanBody body;
    public PostmanUrl url;
    
    public HttpEntity getData(PostmanVariables var) {
        if (body == null || body.mode == null)  {
            return new StringEntity("", ContentType.APPLICATION_JSON);
        } else {
            switch (body.mode) {
                case "raw":
                    return new StringEntity(var.replace(body.raw), ContentType.APPLICATION_JSON);
                case "urlencoded":
                    return urlFormEncodeData(var, body.urlencoded);
                case "formdata":
                    return urlFormData(var, body.urlencoded);
                default:
                    return new StringEntity("", ContentType.APPLICATION_JSON);
            }
        }
	}

    private StringEntity urlFormEncodeData(PostmanVariables var, List<PostmanUrlEncoded> formData) {
        String result = "";
        int i = 0;
        for (PostmanUrlEncoded encoded : formData) {
            result += encoded.key + "=" + URLEncoder.encode(var.replace(encoded.value));
            if (i < formData.size() - 1) {
                result += "&";
            }
        }
        return new StringEntity(result, ContentType.APPLICATION_FORM_URLENCODED);
    }

    private HttpEntity urlFormData(PostmanVariables var, List<PostmanUrlEncoded> urlencoded) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (PostmanUrlEncoded encoded : urlencoded) {
            if (encoded.type.equals("file")) {
                try {
                    File file = new File(encoded.src);
                    String contentType = URLConnection.guessContentTypeFromName(file.getName());
                    FileInputStream fis = new FileInputStream(file);
                    builder.addBinaryBody(encoded.key, fis, ContentType.create(contentType), file.getName());
                } catch (Exception e) {
                    throw new HaltTestFolderException(e);
                }
            } else {
                builder.addTextBody(encoded.key, encoded.value, ContentType.TEXT_PLAIN);
            }
        }
        return builder.build();
    }
    
    public String getUrl(PostmanVariables var) {
        return var.replace(url.raw);
    }

	public Map<String, String> getHeaders(PostmanVariables var) {
		Map<String, String> result = new HashMap<>();
		if (header == null || header.isEmpty()) {
			return result;
		}
		for (PostmanHeader head : header) {
			if (head.key.toUpperCase().equals(PoyntHttpHeaders.REQUEST_ID_HEADER)) {
				result.put(head.key.toUpperCase(), var.replace(head.value));
			} else {
				result.put(head.key, var.replace(head.value));
			}
		}
		return result;
	}
}

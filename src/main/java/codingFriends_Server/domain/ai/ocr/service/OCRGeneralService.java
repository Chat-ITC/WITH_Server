package codingFriends_Server.domain.ai.ocr.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OCRGeneralService {

    public String processImage(String apiURL, String secretKey, String imageFile) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(30000);

        con.setRequestMethod("POST");

        String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");

        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setRequestProperty("X-OCR-SECRET", secretKey);

        JSONObject json = new JSONObject();
        json.put("version", "V2");
        json.put("requestId", UUID.randomUUID().toString());
        json.put("timestamp", System.currentTimeMillis());

        JSONObject image = new JSONObject();
        image.put("format", "jpg");
        image.put("name", "demo");

        JSONArray images = new JSONArray();
        images.put(image);

        json.put("images", images);

        String postParams = json.toString();

        con.connect();

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        long start = System.currentTimeMillis();

        File file = new File(imageFile);

        writeMultiPart(wr, postParams, file, boundary);

        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader br;

        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        StringBuilder responseBuffer = new StringBuilder();
        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            responseBuffer.append(inputLine);
        }

        br.close();

        return extractInferText(responseBuffer.toString()).toString();
    }

    private void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }
            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

    private String extractInferText(String response) {
        List<String> result = new ArrayList<>();
        String Str_result;

        JSONObject jobj = new JSONObject(response);
        JSONArray imagesArray = jobj.getJSONArray("images");

        if (imagesArray.length() > 0) {
            JSONObject imageObj = imagesArray.getJSONObject(0);
            JSONArray fieldsArray = imageObj.getJSONArray("fields");

            for (int i = 0; i < fieldsArray.length(); i++) {
                JSONObject fieldObj = fieldsArray.getJSONObject(i);
                String inferText = fieldObj.getString("inferText");
                result.add(inferText);
            }
        }
        Str_result = result.toString().replaceAll(",", "").replace("[","").replace("]","");
        return Str_result;
    }
}

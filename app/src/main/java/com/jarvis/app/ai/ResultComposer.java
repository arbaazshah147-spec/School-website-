package com.jarvis.app.ai;

public class ResultComposer {

    public String composeResult(String geminiResult, String chatgptResult, String grokResult) {
        StringBuilder finalResult = new StringBuilder();

        if (geminiResult != null && !geminiResult.isEmpty()) {
            finalResult.append("Planning: ").append(geminiResult).append("\n\n");
        }
        if (chatgptResult != null && !chatgptResult.isEmpty()) {
            finalResult.append("Personalization: ").append(chatgptResult).append("\n\n");
        }
        if (grokResult != null && !grokResult.isEmpty()) {
            finalResult.append("Analysis: ").append(grokResult).append("\n");
        }

        if(finalResult.length() == 0){
            return "I was unable to process that request.";
        }

        return finalResult.toString().trim();
    }

     public String parseGeminResponse(String responseBody){
         // Basic parsing, would need to be more robust for production
         return responseBody;
     }

     public String parseChatGPTResponse(String responseBody){
         // Basic parsing
         return responseBody;
     }

     public String parseGrokResponse(String responseBody){
        // Basic parsing
        return responseBody;
     }
}

package com.company;

public class WordleModel { //todo EVERYTHING
    private String word = "AFTER";

    public Response[] submit(String guess) {
        Response[] response = new Response[5];
        for (int i = 0; i < 5; i++) {
            if (word.indexOf(guess.charAt(i)) == i) {
                response[i] = Response.PLACE;
            } else if (word.indexOf(guess.charAt(i)) != -1) {
                response[i] = Response.LETTER;
            } else {
                response[i] = Response.WRONG;
            }
        }

        return response;
    }
}

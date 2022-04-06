package com.fintech.transactions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.transactions.controller.TransactionController;
import com.fintech.transactions.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
        Transaction invalidTransaction  = new Transaction("","",null,null,"");
        String body = objectMapper.writeValueAsString(invalidTransaction);

        mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}

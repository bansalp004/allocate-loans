package com.affirm.allocateloans;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/loan")
public class TestApi {



    //TODO
    @PutMapping(
            value = "/assign",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public boolean assignLoans(
    ) {

        return true;
    }
}

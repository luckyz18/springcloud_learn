package com.itmuch.contentcenter.controller.content;

import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.service.content.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shares")
public class ShareController {
    @Autowired
    private ShareService shareService;

    @GetMapping ("/{id}")
    public ShareDto findById(@PathVariable Integer id){
        return shareService.findById(id);
    }
}

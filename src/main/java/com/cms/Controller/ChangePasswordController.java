package com.cms.Controller;

import com.cms.Service.ChangePwService;
import com.cms.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/change-password")
public class ChangePasswordController {

    @Autowired
    ChangePwService changePwService;

    @Autowired
    LoginService loginService;





}

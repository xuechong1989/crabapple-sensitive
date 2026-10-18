package xc.crabapple.sensitive.module.auth;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/crabapple/sensitive/auth")
public class AuthController {

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public S2cDto login() {
        S2cDto s2cDto = MM.authService.login();
        return s2cDto;
    }

    @RequestMapping(value = "/generate", method = {RequestMethod.POST})
    @ResponseBody
    public S2cDto generate() {
        S2cDto s2cDto = MM.authService.generate();
        return s2cDto;
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    @ResponseBody
    public S2cDto getList() {
        S2cDto s2cDto = MM.authService.getList();
        return s2cDto;
    }

}

package xc.crabapple.sensitive.module.log;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/crabapple/sensitive/log")
public class LogController {

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    @ResponseBody
    public S2cDto getList() {
        S2cDto s2cDto = MM.logService.getList();
        return s2cDto;
    }

}

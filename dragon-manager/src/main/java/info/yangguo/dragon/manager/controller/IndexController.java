package info.yangguo.dragon.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:杨果
 * @date:16/6/22 下午1:42
 *
 * Description:
 *
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }
}

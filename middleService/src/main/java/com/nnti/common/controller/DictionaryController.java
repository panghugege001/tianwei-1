package com.nnti.common.controller;

import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by wander on 2017/2/8.
 */
@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {

    @Autowired
    private IDictionaryService dictionaryService;

    @RequestMapping("/main")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView("base/dictionary");
        return model;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ModelAndView query(Dictionary dictionary) throws Exception {
        ModelAndView model = new ModelAndView("base/dictionary_list");
        List<Dictionary> data = dictionaryService.findByCondition(dictionary);
        model.addObject("data", data);
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(Long id) throws Exception {
        dictionaryService.delete(id);
        return SUCCESS;
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit(Long id) throws Exception {
        ModelAndView model = new ModelAndView("base/dictionary_modal");
        Dictionary vo = dictionaryService.get(id);
        model.addObject("vo", vo);
        return model;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(Long id) {
        ModelAndView model = new ModelAndView("base/dictionary_modal");
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/add_update", method = RequestMethod.POST)
    public String add_update(Dictionary dictionary) throws Exception {
        if (MyUtils.isNotEmpty(dictionary.getId())) {
            dictionaryService.update(dictionary);
        } else {
            dictionaryService.add(dictionary);
        }
        return SUCCESS;
    }
}

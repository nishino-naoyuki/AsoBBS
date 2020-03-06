package jp.ac.asojuku.asobbs.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * エラー画面表示処理
 * 
 * @author nishino
 *
 */
@Controller
@RequestMapping(value= {"/error"})
public class ErrorController {

	/**
	 * 権限エラー画面表示
	 * 
	 * @param mv
	 * @returnzsa
	 */
	@RequestMapping(value= {"/accessdeny"}, method=RequestMethod.GET)
    public ModelAndView input(ModelAndView mv) {
		
        mv.setViewName("/error/accessdeny");
        
        return mv;
    }

	/**
	 * ４０４画面表示
	 * 
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/404"}, method=RequestMethod.GET)
    public ModelAndView err404(ModelAndView mv) {
		
        mv.setViewName("/error/404");
        
        return mv;
    }
}

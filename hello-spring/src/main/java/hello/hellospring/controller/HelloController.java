package hello.hellospring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")        //hello 라는 get을 받으면 하단 함수를 실행한다
    public String hello(Model model){
        model.addAttribute("data","치환되는 데이터!!");
        return "hello";     //return hello의 기능은 resources:templates/ 에있는 hello.html을 찾아서 랜더링을 함.
    }

    @GetMapping("hello-mvc") //value ="name ", required = false로 주면 param이 없어도 에러가안남. 기본은 true
    public String helloMvc(@RequestParam(value = "name", required = false) String name, Model model){
        model.addAttribute("name",name);        //뒤의 값을 "name" 이라는 키값으로 넘김
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody       //직접 바디에 넣어줌 그대로내려감.
    public String helloString(@RequestParam(value = "name", required = false) String name){
        return "hello" + name;
    }

    @GetMapping("hello-api")
    @ResponseBody       //직접 바디에 넣어줌 그대로내려감.
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    static class Hello{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    //이렇게 객체를 넘겨주면, Json방식으로 넘겨짐


}

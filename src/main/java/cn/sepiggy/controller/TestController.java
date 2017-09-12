package cn.sepiggy.controller;

import cn.sepiggy.pojo.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Test controller.
 */
@Controller
public class TestController {

    /**
     * 基本类型数据绑定
     * http://localhost:8080/baseType.do?xage=25
     *
     * @param age the age
     * @return the string
     */
    @RequestMapping(value = "baseType.do")
    @ResponseBody
    public String baseType(@RequestParam("xage") int age) {
        return "age:" + age;
    }

    /**
     * 包装类型数据绑定
     * http://localhost:8080/baseType2.do?age=25
     *
     * @param age the age
     * @return the string
     */
    @RequestMapping(value = "baseType2.do")
    @ResponseBody
    public String baseType2(Integer age) {
        return "age:" + age;
    }

    /**
     * 数组类型数据绑定
     * http://localhost:8080/array.do?name=Tom&name=Lucy&name=Jim
     *
     * @param name the name
     * @return the string
     */
    @RequestMapping(value = "array.do")
    @ResponseBody
    public String array(String[] name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : name) {
            stringBuilder.append(item).append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 简单对象类型数据绑定
     * http://localhost:8080/object.do?name=Tom&age=10
     * <p>
     * 多层级对象类型数据绑定
     * http://localhost:8080/object.do?name=Tom&age=10&contactInfo.phone=10086
     * <p>
     * 同属性的多对象的数据绑定
     * http://localhost:8080/object.do?user.name=Tom&admin.name=Lucy&age=10
     *
     * @param user the user
     * @return the string
     */
    @RequestMapping(value = "object.do")
    @ResponseBody
    public String object(User user, Admin admin) {
        return user.toString() + " " + admin.toString();
    }

    @InitBinder("user")
    public void initUser(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    @InitBinder("admin")
    public void initAdmin(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("admin.");
    }

    /**
     * List对象的数据绑定
     * http://localhost:8080/list.do?users[0].name=Tom&users[1].name=Lucy
     * http://www.localhost.com:8080/list.do?users[0].name=Tom&users[1].name=Lucy&users[20].name=Jim (该请求 List 实际大小是 21)
     *
     * @param userListForm the user list form
     * @return the string
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public String list(UserListForm userListForm) {
        return "listSize:" + userListForm.getUsers().size() + " " + userListForm.toString();
    }

    /**
     * Set对象的数据绑定
     * http://localhost:8080/set.do?users[0].name=Tom&users[1].name=Lucy
     * [坑]SpringMvc的Set绑定支持不是很友好, 需要提前初始化Set, 使其Size>0. 尽量使用Map和List来代替Set.
     *
     * @param userSetForm the user set form
     * @return the string
     */
    @RequestMapping(value = "set.do")
    @ResponseBody
    public String list(UserSetForm userSetForm) {
        return userSetForm.toString();
    }

    /**
     * Map对象的数据绑定
     * http://localhost:8080/map.do?users['X'].name=Tom&users['X'].age=10&users['Y'].name=Lucy
     *
     * @param userMapForm the user map form
     * @return the string
     */
    @RequestMapping(value = "map.do")
    @ResponseBody
    public String map(UserMapForm userMapForm) {
        return userMapForm.toString();
    }

    /**
     * Json对象的数据绑定
     * http://localhost:8080/json.do
     * application/json
     * {
     * "name": "Jim",
     * "age": 16,
     * "contactInfo": {
     * "address": "beijing",
     * "phone":	"10010"
     * }
     * }
     *
     * @param user the user
     * @return the string
     */
    @RequestMapping(value = "json.do")
    @ResponseBody
    public String json(@RequestBody User user) {
        return user.toString();
    }

    /**
     * Xml对象的数据绑定
     * http://localhost:8080/xml.do
     * application/xml
     * <?xml version="1.0" encoding="UTF-8" ?>
     * <admin>
     * <name>Jim</name>
     * <age>16</age>
     * </admin>
     *
     * @param admin the admin
     * @return the string
     */
    @RequestMapping(value = "xml.do")
    @ResponseBody
    public String xml(@RequestBody Admin admin) {
        return admin.toString();
    }

    /**
     * Boolean对象转化为String对象
     * http://localhost:8080/converter.do?bool=yes
     * http://localhost:8080/converter.do?bool=on
     * http://localhost:8080/converter.do?bool=1
     * 以上三个都绑定到true
     *
     * @param bool the bool
     * @return the string
     */
    @RequestMapping(value = "converter.do")
    @ResponseBody
    public String converter(Boolean bool) {
        return bool.toString();
    }

    /**
     * 日期类型数据绑定
     * http://localhost:8080/date1.do?date1=2018-01-05
     *
     * @param date1 the date 1
     * @return the string
     */
    @RequestMapping(value = "date1.do")
    @ResponseBody
    public String date1(Date date1) {
        return date1.toString();
    }

    // @InitBinder("date1")
    // public void initDate1(WebDataBinder binder) {
    //     binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    // }

    /**
     * 使用自定义Formatter实现全局日期类型数据绑定
     * http://localhost:8080/date1.do?date1=2018-01-05
     *
     * @param date2 the date 2
     * @return the string
     */
    @RequestMapping(value = "date2.do")
    @ResponseBody
    public String date2(Date date2) {
        return date2.toString();
    }


    /**
     * book是一个资源, 它的表现形式不能通过URL体现, 需要借助Content-Type体现.
     * URI ---> 代表资源位置
     * Content-Type ---> 代表资源展现形式(eg. 文本资源的展现形式可以是: txt, html, xml, json ...)
     *
     * @param request the request
     * @return the string
     */
    @RequestMapping(value = "/book", method = RequestMethod.GET)
    @ResponseBody
    public String book(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return "book.default";
        } else if (contentType.equals("txt")) {
            return "book.txt";
        } else if (contentType.equals("html")) {
            return "book.html";
        }
        return "book.default";
    }

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.GET)
    @ResponseBody
    public String subjectGet(@PathVariable("subjectId") String subjectId) {
        return "this is a GET method, subjectId:" + subjectId;
    }

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.POST)
    @ResponseBody
    public String subjectPost(@PathVariable("subjectId") String subjectId) {
        return "this is a POST method, subjectId:" + subjectId;
    }

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String subjectDelete(@PathVariable("subjectId") String subjectId) {
        return "this is a DELETE method, subjectId:" + subjectId;
    }

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.PUT)
    @ResponseBody
    public String subjectPUT(@PathVariable("subjectId") String subjectId) {
        return "this is a PUT method, subjectId:" + subjectId;
    }


}




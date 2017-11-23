package com.tr1nks.emailbase.controller;

import com.tr1nks.emailbase.model.engines.UploadEngine;
import com.tr1nks.emailbase.model.models.Model;
import com.tr1nks.emailbase.model.models.StudentModel;
import com.tr1nks.emailbase.model.models.TeacherModel;
import com.tr1nks.emailbase.model.models.UploadModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * контроллер страницы загрузки на сервер
 */
@Controller
@RequestMapping({"/upload"})
public class UploadController {
    private static final String VIEW_NAME = "upload";
    private static final String MODEL_NAME = "uploadPD";
    @Resource
    private UploadEngine uploadEngine;

    /**
     * GET mapping обработчик
     *
     * @return имя view
     */
    @GetMapping
    public String get() {
        return VIEW_NAME;
    }

    private static final String STUDENT_SAMPLE_STR = "Фамилия;Имя;Отчество;Код;Группа;Бюджет\nИванов;Иван;Иванович;co32432de;6.1.11.11.11;true";
    private static final String TEACHER_SAMPLE_STR = "Фамилия;Имя;Отчество;Код;Группа;Бюджет\nИванов;Иван;Иванович;co32432de;6.1.11.11.11;true";//todo remake with teacher data

    /**
     * GET mapping обработчик для получения примера файла
     *
     * @param person   тип получаемого файла (студент преподаватель)
     * @param response ответ сервера для копирования файла
     * @return имя представления
     */
    @GetMapping(path = "sample/{person}")
    public String getSample(@PathVariable("person") String person, HttpServletResponse response) {
        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream())) {
            response.setContentType("text/csv");
            if ("student".equals(person.toLowerCase())) {
                writer.write(STUDENT_SAMPLE_STR);
            } else if ("teacher".equals(person.toLowerCase())) {
                writer.write(TEACHER_SAMPLE_STR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return VIEW_NAME;
    }

    /**
     * POST mapping обработчик загрузки файла на сервер
     *
     * @param file файл
     * @return имя представления и модель с данными для отображения и настройки фильтров
     */
    @PostMapping(path = "file")
    public ModelAndView postFile(@RequestParam("file") MultipartFile file) {
        UploadModel u = null;
        if (!file.isEmpty()) {
            u = uploadEngine.uploadFile(file);
        }
        return new ModelAndView(VIEW_NAME, MODEL_NAME, u);
    }

    /**
     * POST mapping обработчик проверки фильтров
     *
     * @param uploadPD данные страницы
     * @return имя представления и данные страницы с учетом фильтров
     */
    @PostMapping(path = "test")
    public ModelAndView postTest(@ModelAttribute(MODEL_NAME) UploadModel uploadPD) {
        uploadEngine.refillWithNewFilterData(uploadPD);
        return new ModelAndView(VIEW_NAME, MODEL_NAME, uploadPD);
    }

    /**
     * POST mapping обработчик парсинга файла
     *
     * @param uploadPD данные страницы
     * @return имя представления на которое будет перенамправлене и данные страницы этого представления
     */
    @PostMapping(path = "process")
    public ModelAndView postProcess(@ModelAttribute(MODEL_NAME) UploadModel uploadPD) {
        Model pd = uploadEngine.parseFromFile(uploadPD);
        if (pd instanceof StudentModel) {
//            return studentController.post((StudentPageData) pd);
            return new ModelAndView("/students", "studentsPD", pd);
        } else if (pd instanceof TeacherModel) {
            return new ModelAndView("/teachers", "teachersPD", pd);//fixme
        } else {
            return new ModelAndView("/error", "", null);//fixme error here
        }
    }
}

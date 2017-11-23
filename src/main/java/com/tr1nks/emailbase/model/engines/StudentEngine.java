package com.tr1nks.emailbase.model.engines;

import com.tr1nks.emailbase.model.entities.StudentEntity;
import com.tr1nks.emailbase.model.models.StudentModel;
import com.tr1nks.emailbase.model.services.FacultyService;
import com.tr1nks.emailbase.model.services.GroupService;
import com.tr1nks.emailbase.model.services.StudentService;
import com.tr1nks.emailbase.model.utils.FileGenerator;
import com.tr1nks.emailbase.model.utils.MyMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * обработчик для контроллера {@link com.tr1nks.emailbase.controller.StudentController StudentController}
 */
@Component
public class StudentEngine {
    @Resource
    private StudentService studentService;
    @Resource
    private GroupService groupService;
    @Resource
    private FacultyService facultyService;
    @Resource
    private FileGenerator fileGenerator;
    @Resource
    private MyMailSender myMailSender;

    /**
     * обработчик get действия
     *
     * @return данные страницы студенты с данными для фильтрации
     */
    public StudentModel get() {
        StudentModel studentPD = new StudentModel();
        fillFilterData(studentPD);
        return studentPD;
    }

    /**
     * заполняет данные фильтров и выбирает по установленным фильтрам студентов
     *
     * @param studentPD данные страницы для заполнения в них фильтров
     */
    public void fillFilterData(StudentModel studentPD) {
        studentPD.setVariantFacults(facultyService.getAll());
        studentPD.setVariantGroups(groupService.getAll());
        studentPD.setVariantYears(groupService.getYears());
        HashSet<StudentEntity> students = new HashSet<>();
        if (null != studentPD.getFilterFacults() && !studentPD.getFilterFacults().isEmpty()) {
            studentPD.getFilterFacults().forEach(integer -> students.addAll(studentService.getStudentsByFacultyId(integer)));
        }
        if (null != studentPD.getFilterGroups() && !studentPD.getFilterGroups().isEmpty()) {
            studentPD.getFilterGroups().forEach(integer -> students.addAll(studentService.getStudentsByGroupId(integer)));
        }
        if (null != studentPD.getFilterYears() && !studentPD.getFilterYears().isEmpty()) {
            studentPD.getFilterYears().forEach(integer -> students.addAll(studentService.getStudentsByYear(integer)));
        }
        if (!students.isEmpty()) {
            if (studentPD.getStudents() == null) {
                studentPD.setStudents(new ArrayList<>());
            } else {
                studentPD.getStudents().clear();
            }
            studentPD.getStudents().addAll(students);
        }
    }

    /**
     * установить снять метку бюджета
     *
     * @param studentPD данные страницы
     * @param value     true - для установки бюджета, false - для снятия
     */
    public void budget(StudentModel studentPD, boolean value) {
        studentPD.getStudents().stream().filter(se -> studentPD.getSelectedStudents().contains(se.getId())).forEach(studentEntity -> {
            studentEntity.setBudget(value);
            studentService.editStudent(studentEntity);
        });
    }

    /**
     * установить снять imagine аккаунт метку
     *
     * @param studentPD данные страницы
     * @param value     true - для установки imagine, false - для снятия
     */
    public void imagine(StudentModel studentPD, boolean value) {
        studentPD.getStudents().stream().filter(se -> studentPD.getSelectedStudents().contains(se.getId())).forEach(studentEntity -> {
            studentEntity.setImagine(value);
            studentService.editStudent(studentEntity);
        });
    }

    /**
     * установить снять office аккаунт метку
     *
     * @param studentPD данные страницы
     * @param value     true - для установки office, false - для снятия
     */
    public void office(StudentModel studentPD, boolean value) {
        studentPD.getStudents().stream().filter(se -> studentPD.getSelectedStudents().contains(se.getId())).forEach(studentEntity -> {
            studentEntity.setOffice(value);
            studentService.editStudent(studentEntity);
        });
    }

    /**
     * отправить данные по эл. почте
     *
     * @param studentPD данные страницы
     */
    public void sendData(StudentModel studentPD, String userLogin) {
        List st = studentService.getAllByCode(studentPD.getSelectedStudents());
        myMailSender.sendTLSMailPropFileText(fileGenerator.createFullPersonsCsvs(st), userLogin);
    }

    /**
     * создать zip архив pdf
     *
     * @param studentPD студенты для создания архива
     * @return массив байт архива
     */
    public byte[] createPDFArchive(StudentModel studentPD) {
        List st = studentService.getAllByCode(studentPD.getSelectedStudents());
        return fileGenerator.createPDFArchiveBytes(st);
    }

    public void commitEdit(StudentModel studentPD) {
        studentPD.getStudents().stream().filter(student -> studentPD.getSelectedStudents().contains(student.getId())).forEach(student -> {
            studentService.editStudent(student);
        });
        fillFilterData(studentPD);
    }
}

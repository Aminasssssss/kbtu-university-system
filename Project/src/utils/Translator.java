package utils;

import enums.Language;

/**
 * Utility class that provides translated strings for the entire UI.
 * Supports EN (English), RU (Russian), and KZ (Kazakh).
 * All menu labels, prompts, and messages are translated here.
 */
public class Translator {

    private Translator() {}

    /**
     * Returns a translated string for the given key and language.
     *
     * @param key      the translation key
     * @param language the target language
     * @return translated string
     */
    public static String get(String key, Language language) {
        if (language == Language.RU) {
            return getRU(key);
        }
        if (language == Language.KZ) {
            return getKZ(key);
        }
        return getEN(key);
    }

    private static String getEN(String key) {
        switch (key) {
            case "welcome": return "Welcome";
            case "logout": return "Logged out.";
            case "invalid": return "Invalid option.";
            case "choose": return "Choose: ";
            case "login": return "1. Login";
            case "exit": return "0. Exit";
            case "logout_option": return "0.  Logout";
            case "back": return "0. Back";
            case "no_courses": return "No courses available.";
            case "lang_changed": return "Language changed.";
            case "lang_select": return "Select language:\n1. English (EN)\n2. Қазақша (KZ)\n3. Русский (RU)";

            case "view_courses": return "1.  View available courses";
            case "register_course": return "2.  Register for a course";
            case "view_marks": return "3.  View my marks";
            case "view_transcript": return "4.  View transcript";
            case "rate_teacher": return "5.  Rate a teacher";
            case "teacher_info": return "6.  Teacher info for a course";
            case "organizations": return "7.  Student organizations";
            case "journals": return "8.  Journal subscriptions";
            case "tech_request": return "9.  Submit tech support request";
            case "change_language": return "10. Change language";
            case "grad_info": return "11. Graduate student info";
            case "researcher_tools": return "12. Researcher tools";
            case "drop_course": return "13. Drop a course";
            case "view_news": return "14. View university news";
            case "student_menu_title": return "STUDENT MENU";
            case "no_marks": return "No marks yet.";
            case "already_enrolled": return "You are already enrolled in this course.";
            case "registered_for": return "Successfully registered for: ";
            case "total_credits": return "Total credits: ";
            case "dropped": return "Dropped: ";
            case "remaining_credits": return "Remaining credits: ";
            case "no_news": return "No news published yet.";
            case "enter_comment": return "Your comment: ";
            case "comment_added": return "Comment added!";
            case "add_comment": return "C. Leave a comment";

            case "teacher_menu_title": return "TEACHER MENU";
            case "my_courses": return "1.  View my courses";
            case "put_mark": return "2.  Put mark";
            case "send_complaint": return "3.  Send complaint";
            case "view_students": return "4.  View all students";
            case "send_message": return "5.  Send message";
            case "marks_report": return "7.  Marks report";
            case "journal_sub": return "8.  Journal subscriptions";
            case "tech_req": return "9.  Submit tech request";
            case "lang": return "10. Change language";
            case "researcher": return "11. Researcher tools";
            case "no_students_enrolled": return "No students enrolled in this course.";
            case "mark_saved": return "Mark saved.";
            case "select_course": return "Select course number: ";
            case "select_student": return "Select student number: ";
            case "enter_att1": return "Attestation 1 (0-30): ";
            case "enter_att2": return "Attestation 2 (0-30): ";
            case "enter_final": return "Final exam (0-40): ";
            case "complaint_sent": return "Complaint filed.";
            case "enter_description": return "Describe the issue: ";

            case "manager_menu_title": return "MANAGER MENU";
            case "assign_course": return "1.  Assign course to teacher";
            case "approve_reg": return "2.  Approve student registration";
            case "add_course": return "3.  Add new course";
            case "students_sorted": return "4.  View students sorted";
            case "view_teachers": return "5.  View teachers";
            case "statistics": return "6.  Statistics report";
            case "manage_news": return "7.  Manage news";
            case "view_requests": return "8.  View tech requests";
            case "view_complaints": return "9.  View complaints";
            case "change_lang": return "10. Change language";
            case "course_assigned": return "Course assigned.";
            case "approved": return "Approved.";

            case "admin_menu_title": return "ADMIN MENU";
            case "view_users": return "1. View all users";
            case "add_student": return "2. Add student";
            case "remove_user": return "3. Remove user";
            case "view_logs": return "4. View logs";
            case "search_user": return "5. Search user by email";
            case "user_added": return "Student added.";
            case "user_removed": return "User removed.";
            case "user_not_found": return "User not found.";

            case "dean_menu_title": return "DEAN MENU";
            case "view_complaints_dean": return "1. View all complaints";
            case "resolve_complaint": return "2. Resolve a complaint";
            case "sign_request": return "3. Sign a tech request";
            case "view_logs_dean": return "4. View system logs";
            case "complaint_resolved": return "Complaint resolved.";
            case "no_complaints": return "No complaints.";

            case "tech_menu_title": return "TECH SUPPORT MENU";
            case "view_all_requests": return "1. View all requests";
            case "accept_request": return "2. Accept a request";
            case "reject_request": return "3. Reject a request";
            case "mark_done": return "4. Mark as Done";
            case "no_requests": return "No requests yet.";

            case "inbox": return "6.  Inbox";
            case "no_messages": return "No messages.";
            case "message_from": return "From: ";
            case "message_content": return "Message: ";

            case "attendance": return "12. Mark attendance";
            case "present": return "Present";
            case "absent": return "Absent";
            case "attendance_marked": return "Attendance marked.";

            default: return key;
        }
    }

    private static String getRU(String key) {
        switch (key) {
            case "welcome": return "Добро пожаловать";
            case "logout": return "Выход выполнен.";
            case "invalid": return "Неверный вариант.";
            case "choose": return "Выберите: ";
            case "login": return "1. Войти";
            case "exit": return "0. Выход";
            case "logout_option": return "0.  Выход из аккаунта";
            case "back": return "0. Назад";
            case "no_courses": return "Курсы недоступны.";
            case "lang_changed": return "Язык изменён.";
            case "lang_select": return "Выберите язык:\n1. English (EN)\n2. Қазақша (KZ)\n3. Русский (RU)";

            case "view_courses": return "1.  Просмотр доступных курсов";
            case "register_course": return "2.  Записаться на курс";
            case "view_marks": return "3.  Мои оценки";
            case "view_transcript": return "4.  Транскрипт";
            case "rate_teacher": return "5.  Оценить преподавателя";
            case "teacher_info": return "6.  Информация о преподавателе";
            case "organizations": return "7.  Студенческие организации";
            case "journals": return "8.  Подписка на журналы";
            case "tech_request": return "9.  Запрос в техподдержку";
            case "change_language": return "10. Сменить язык";
            case "grad_info": return "11. Информация для аспиранта";
            case "researcher_tools": return "12. Инструменты исследователя";
            case "drop_course": return "13. Отменить запись на курс";
            case "view_news": return "14. Новости университета";
            case "student_menu_title": return "МЕНЮ СТУДЕНТА";
            case "no_marks": return "Оценок пока нет.";
            case "already_enrolled": return "Вы уже записаны на этот курс.";
            case "registered_for": return "Успешно записаны на: ";
            case "total_credits": return "Всего кредитов: ";
            case "dropped": return "Курс отменён: ";
            case "remaining_credits": return "Осталось кредитов: ";
            case "no_news": return "Новостей пока нет.";
            case "enter_comment": return "Ваш комментарий: ";
            case "comment_added": return "Комментарий добавлен!";
            case "add_comment": return "C. Оставить комментарий";

            case "teacher_menu_title": return "МЕНЮ ПРЕПОДАВАТЕЛЯ";
            case "my_courses": return "1.  Мои курсы";
            case "put_mark": return "2.  Поставить оценку";
            case "send_complaint": return "3.  Подать жалобу";
            case "view_students": return "4.  Все студенты";
            case "send_message": return "5.  Отправить сообщение";
            case "marks_report": return "7.  Отчёт по оценкам";
            case "journal_sub": return "8.  Подписка на журналы";
            case "tech_req": return "9.  Запрос в техподдержку";
            case "lang": return "10. Сменить язык";
            case "researcher": return "11. Инструменты исследователя";
            case "no_students_enrolled": return "На этот курс нет записанных студентов.";
            case "mark_saved": return "Оценка сохранена.";
            case "select_course": return "Выберите номер курса: ";
            case "select_student": return "Выберите номер студента: ";
            case "enter_att1": return "Аттестация 1 (0-30): ";
            case "enter_att2": return "Аттестация 2 (0-30): ";
            case "enter_final": return "Итоговый экзамен (0-40): ";
            case "complaint_sent": return "Жалоба подана.";
            case "enter_description": return "Опишите проблему: ";

            case "manager_menu_title": return "МЕНЮ МЕНЕДЖЕРА";
            case "assign_course": return "1.  Назначить курс преподавателю";
            case "approve_reg": return "2.  Одобрить запись студента";
            case "add_course": return "3.  Добавить новый курс";
            case "students_sorted": return "4.  Студенты (сортировка)";
            case "view_teachers": return "5.  Преподаватели";
            case "statistics": return "6.  Статистика";
            case "manage_news": return "7.  Управление новостями";
            case "view_requests": return "8.  Технические запросы";
            case "view_complaints": return "9.  Жалобы";
            case "change_lang": return "10. Сменить язык";
            case "course_assigned": return "Курс назначен.";
            case "approved": return "Одобрено.";

            case "admin_menu_title": return "МЕНЮ АДМИНИСТРАТОРА";
            case "view_users": return "1. Все пользователи";
            case "add_student": return "2. Добавить студента";
            case "remove_user": return "3. Удалить пользователя";
            case "view_logs": return "4. Журнал действий";
            case "search_user": return "5. Поиск по email";
            case "user_added": return "Студент добавлен.";
            case "user_removed": return "Пользователь удалён.";
            case "user_not_found": return "Пользователь не найден.";

            case "dean_menu_title": return "МЕНЮ ДЕКАНА";
            case "view_complaints_dean": return "1. Все жалобы";
            case "resolve_complaint": return "2. Решить жалобу";
            case "sign_request": return "3. Подписать запрос";
            case "view_logs_dean": return "4. Системный журнал";
            case "complaint_resolved": return "Жалоба решена.";
            case "no_complaints": return "Жалоб нет.";

            case "tech_menu_title": return "МЕНЮ ТЕХПОДДЕРЖКИ";
            case "view_all_requests": return "1. Все запросы";
            case "accept_request": return "2. Принять запрос";
            case "reject_request": return "3. Отклонить запрос";
            case "mark_done": return "4. Отметить как выполнено";
            case "no_requests": return "Запросов пока нет.";

            case "inbox": return "6.  Входящие сообщения";
            case "no_messages": return "Сообщений нет.";
            case "message_from": return "От: ";
            case "message_content": return "Сообщение: ";

            case "attendance": return "12. Отметить посещаемость";
            case "present": return "Присутствует";
            case "absent": return "Отсутствует";
            case "attendance_marked": return "Посещаемость отмечена.";

            default: return key;
        }
    }

    private static String getKZ(String key) {
        switch (key) {
            case "welcome": return "Қош келдіңіз";
            case "logout": return "Жүйеден шықтыңыз.";
            case "invalid": return "Қате таңдау.";
            case "choose": return "Таңдаңыз: ";
            case "login": return "1. Кіру";
            case "exit": return "0. Шығу";
            case "logout_option": return "0.  Аккаунттан шығу";
            case "back": return "0. Артқа";
            case "no_courses": return "Курстар жоқ.";
            case "lang_changed": return "Тіл өзгертілді.";
            case "lang_select": return "Тілді таңдаңыз:\n1. English (EN)\n2. Қазақша (KZ)\n3. Русский (RU)";

            case "view_courses": return "1.  Қол жетімді курстар";
            case "register_course": return "2.  Курсқа тіркелу";
            case "view_marks": return "3.  Менің бағаларым";
            case "view_transcript": return "4.  Транскрипт";
            case "rate_teacher": return "5.  Оқытушыны бағалау";
            case "teacher_info": return "6.  Оқытушы туралы ақпарат";
            case "organizations": return "7.  Студенттік ұйымдар";
            case "journals": return "8.  Журналға жазылу";
            case "tech_request": return "9.  Техникалық сұраныс";
            case "change_language": return "10. Тілді өзгерту";
            case "grad_info": return "11. Магистрант ақпараты";
            case "researcher_tools": return "12. Зерттеуші құралдары";
            case "drop_course": return "13. Курстан шығу";
            case "view_news": return "14. Университет жаңалықтары";
            case "student_menu_title": return "СТУДЕНТ МӘЗІРІ";
            case "no_marks": return "Бағалар жоқ.";
            case "already_enrolled": return "Сіз бұл курсқа тіркелгенсіз.";
            case "registered_for": return "Сәтті тіркелдіңіз: ";
            case "total_credits": return "Жалпы кредит: ";
            case "dropped": return "Курстан шықтыңыз: ";
            case "remaining_credits": return "Қалған кредит: ";
            case "no_news": return "Жаңалықтар жоқ.";
            case "enter_comment": return "Пікіріңіз: ";
            case "comment_added": return "Пікір қосылды!";
            case "add_comment": return "C. Пікір қалдыру";

            case "teacher_menu_title": return "ОҚЫТУШЫ МӘЗІРІ";
            case "my_courses": return "1.  Менің курстарым";
            case "put_mark": return "2.  Баға қою";
            case "send_complaint": return "3.  Шағым жіберу";
            case "view_students": return "4.  Барлық студенттер";
            case "send_message": return "5.  Хабарлама жіберу";
            case "marks_report": return "7.  Бағалар есебі";
            case "journal_sub": return "8.  Журналға жазылу";
            case "tech_req": return "9.  Техникалық сұраныс";
            case "lang": return "10. Тілді өзгерту";
            case "researcher": return "11. Зерттеуші құралдары";
            case "no_students_enrolled": return "Бұл курста студент жоқ.";
            case "mark_saved": return "Баға сақталды.";
            case "select_course": return "Курс нөмірін таңдаңыз: ";
            case "select_student": return "Студент нөмірін таңдаңыз: ";
            case "enter_att1": return "1-аттестация (0-30): ";
            case "enter_att2": return "2-аттестация (0-30): ";
            case "enter_final": return "Қорытынды емтихан (0-40): ";
            case "complaint_sent": return "Шағым жіберілді.";
            case "enter_description": return "Мәселені сипаттаңыз: ";

            case "manager_menu_title": return "МЕНЕДЖЕР МӘЗІРІ";
            case "assign_course": return "1.  Курсты оқытушыға тағайындау";
            case "approve_reg": return "2.  Студент тіркелуін мақұлдау";
            case "add_course": return "3.  Жаңа курс қосу";
            case "students_sorted": return "4.  Студенттер тізімі";
            case "view_teachers": return "5.  Оқытушылар";
            case "statistics": return "6.  Статистика";
            case "manage_news": return "7.  Жаңалықтарды басқару";
            case "view_requests": return "8.  Техникалық сұраныстар";
            case "view_complaints": return "9.  Шағымдар";
            case "change_lang": return "10. Тілді өзгерту";
            case "course_assigned": return "Курс тағайындалды.";
            case "approved": return "Мақұлданды.";

            case "admin_menu_title": return "ӘКІМШІ МӘЗІРІ";
            case "view_users": return "1. Барлық пайдаланушылар";
            case "add_student": return "2. Студент қосу";
            case "remove_user": return "3. Пайдаланушыны жою";
            case "view_logs": return "4. Журнал";
            case "search_user": return "5. Email бойынша іздеу";
            case "user_added": return "Студент қосылды.";
            case "user_removed": return "Пайдаланушы жойылды.";
            case "user_not_found": return "Пайдаланушы табылмады.";

            case "dean_menu_title": return "ДЕКАН МӘЗІРІ";
            case "view_complaints_dean": return "1. Барлық шағымдар";
            case "resolve_complaint": return "2. Шағымды шешу";
            case "sign_request": return "3. Сұранысқа қол қою";
            case "view_logs_dean": return "4. Жүйе журналы";
            case "complaint_resolved": return "Шағым шешілді.";
            case "no_complaints": return "Шағым жоқ.";

            case "tech_menu_title": return "ТЕХНИКАЛЫҚ ҚОЛДАУ МӘЗІРІ";
            case "view_all_requests": return "1. Барлық сұраныстар";
            case "accept_request": return "2. Сұранысты қабылдау";
            case "reject_request": return "3. Сұранысты қабылдамау";
            case "mark_done": return "4. Орындалды деп белгілеу";
            case "no_requests": return "Сұраныстар жоқ.";

            case "inbox": return "5.  Кіріс хабарламалар";
            case "no_messages": return "Хабарламалар жоқ.";
            case "message_from": return "Жіберуші: ";
            case "message_content": return "Хабарлама: ";

            case "attendance": return "12. Қатысуды белгілеу";
            case "present": return "Қатысты";
            case "absent": return "Қатыспады";
            case "attendance_marked": return "Қатысу белгіленді.";

            default: return key;
        }
    }
}

package patterns;

import enums.*;
import models.users.*;

/**
 * Factory pattern — centralizes user creation logic.
 * Creates different types of users based on the UserType enum.
 * Hides instantiation details from client code.
 */
public class UserFactory {

    /**
     * Creates a new User based on the specified type.
     * @param type the type of user to create
     * @param id unique user ID
     * @param name full name
     * @param email email address
     * @param password login password
     * @return the created User
     * @throws IllegalArgumentException if the user type is unknown
     */
    public static User createUser(UserType type, String id, String name, String email, String password) {
        switch (type) {
            case STUDENT:
                return new Student(id, name, email, password, Language.EN, 0.0, 0, 0);
            case GRADUATE_STUDENT:
                return new GraduateStudent(id, name, email, password, Language.EN, 0.0, 0, 0,
                        DegreeType.MASTER, null);
            case TEACHER:
                return new Teacher(id, name, email, password, Language.EN, 0.0, "Unknown",
                        TeacherPosition.TUTOR);
            case ADMIN:
                return new Admin(id, name, email, password, Language.EN, 0.0, "Administration");
            case MANAGER:
                return new Manager(id, name, email, password, Language.EN, 0.0, "Management",
                        ManagerType.OR);
            case TECH_SUPPORT_SPECIALIST:
                return new TechSupportSpecialist(id, name, email, password, Language.EN, 0.0,
                        "Tech Support");
            default:
                throw new IllegalArgumentException("Unknown user type");
        }
    }
}
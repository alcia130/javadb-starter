package pl.sda.jpa.starter.relations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaRelations {
    private static Logger logger = LoggerFactory.getLogger(JpaRelations.class);
    private EntityManagerFactory entityManagerFactory;

    public JpaRelations() {
        entityManagerFactory = Persistence.createEntityManagerFactory("pl.sda.jpa.starter.relations");
    }

    public void close() {
        entityManagerFactory.close();
    }

    private void oneToOne() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            AddressEntity address = new AddressEntity("Gdańsk", "Malwinowa 1/3");
            StudentEntity student = new StudentEntity("Jan Kowalski");
            student.setAddress(address);

            entityManager.persist(student);

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);

            AddressEntity addressEntity = entityManager.find(AddressEntity.class, 1);
            logger.info("AddressEntity Student: {}", addressEntity.getStudent());

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void oneToMany() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CourseEntity course = new CourseEntity("JavaGda11", "Sopot");
            StudentEntity student = new StudentEntity("Jan Kowalski");
            course.addStudent(student);

            entityManager.persist(student);

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);

            CourseEntity courseEntity = entityManager.find(CourseEntity.class, 1);
            logger.info("Course: {}", courseEntity);

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void manyToMany() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SkillEntity skill1 = new SkillEntity("JVM Master");
            SkillEntity skill2 = new SkillEntity("JDBC Master");
            SkillEntity skill3 = new SkillEntity("Hibernate Master");
            StudentEntity student = new StudentEntity("Jan Kowalski");
            student.addSkill(skill1);
            student.addSkill(skill2);
            student.addSkill(skill3);

            entityManager.persist(student);

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void addStudentsWithSeats() {
        StudentEntity student = new StudentEntity("Jan Kowalski");
        SeatEntity seat = new SeatEntity("A", 5, 1);
        student.setSeat(seat);

        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(student);

            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    private void addStudentsToCourse() {
        StudentEntity student1 = new StudentEntity("Jan Kowalski");
        StudentEntity student2 = new StudentEntity("Joanna Musik");
        StudentEntity student3 = new StudentEntity("Adam Nowak");

        CourseEntity courseEntity = new CourseEntity("JavaGda1", "Rumia");
        courseEntity.addStudent(student1);
        courseEntity.addStudent(student2);
        courseEntity.addStudent(student3);

        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(student1);
            entityManager.persist(student2);
            entityManager.persist(student3);

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void modifyStudentsListForCourse(int courseId) {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CourseEntity courseEntity = entityManager.find(CourseEntity.class, courseId);
            if (courseEntity != null) {
                StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
                courseEntity.removeStudent(studentEntity);

                studentEntity = entityManager.find(StudentEntity.class, 2);
                studentEntity.setName("Nowy Student");

                StudentEntity newStudent = new StudentEntity("Marta Sowa");
                entityManager.persist(newStudent);

                courseEntity.addStudent(newStudent);

            } else {
                logger.warn("CourseEntity of id:{} - not found !", courseId);
            }

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void removeStudent(int studentId) {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, studentId);
            if (studentEntity != null) {
                entityManager.remove(studentEntity);
            } else {
                logger.warn("StudentEntity of id:{} - not found !", studentId);
            }

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public static void main(String[] args) {
        JpaRelations jpaQueries = new JpaRelations();
        try {
            jpaQueries.oneToOne();
            //jpaQueries.oneToMany();
            //jpaQueries.manyToMany();
            //jpaQueries.addStudentsWithSeats();
            //jpaQueries.addStudentsToCourse();
            //jpaQueries.modifyStudentsListForCourse(1);
            //jpaQueries.removeStudent(1);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            jpaQueries.close();
        }
    }
}
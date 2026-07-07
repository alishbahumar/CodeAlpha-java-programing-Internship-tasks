import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentGradeTracker {

    // ---------- Student class ----------
    static class Student {
        String name;
        List<Double> grades = new ArrayList<>();

        Student(String name) {
            this.name = name;
        }

        void addGrade(double grade) {
            grades.add(grade);
        }

        double average() {
            if (grades.isEmpty()) return 0;
            double sum = 0;
            for (double g : grades) sum += g;
            return sum / grades.size();
        }

        double highest() {
            if (grades.isEmpty()) return 0;
            double max = grades.get(0);
            for (double g : grades) if (g > max) max = g;
            return max;
        }

        double lowest() {
            if (grades.isEmpty()) return 0;
            double min = grades.get(0);
            for (double g : grades) if (g < min) min = g;
            return min;
        }
    }

    static List<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrade();
                case 3 -> viewSummaryReport();
                case 4 -> viewClassStatistics();
                case 5 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice, try again.");
            }
        } while (choice != 5);
        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===== STUDENT GRADE TRACKER =====");
        System.out.println("1. Add Student");
        System.out.println("2. Add Grade to a Student");
        System.out.println("3. View Summary Report (per student)");
        System.out.println("4. View Overall Class Statistics");
        System.out.println("5. Exit");
    }

    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();
        students.add(new Student(name));
        System.out.println("Student '" + name + "' added successfully.");
    }

    static void addGrade() {
        if (students.isEmpty()) {
            System.out.println("No students found. Please add a student first.");
            return;
        }
        Student s = selectStudent();
        if (s == null) return;
        double grade = readDouble("Enter grade to add for " + s.name + ": ");
        s.addGrade(grade);
        System.out.println("Grade " + grade + " added for " + s.name + ".");
    }

    static void viewSummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No students to show.");
            return;
        }
        System.out.println("\n---------- SUMMARY REPORT ----------");
        System.out.printf("%-15s %-8s %-10s %-10s %-10s%n",
                "Name", "Count", "Average", "Highest", "Lowest");
        for (Student s : students) {
            System.out.printf("%-15s %-8d %-10.2f %-10.2f %-10.2f%n",
                    s.name, s.grades.size(), s.average(), s.highest(), s.lowest());
        }
    }

    static void viewClassStatistics() {
        if (students.isEmpty()) {
            System.out.println("No students to show.");
            return;
        }
        List<Double> allGrades = new ArrayList<>();
        for (Student s : students) allGrades.addAll(s.grades);

        if (allGrades.isEmpty()) {
            System.out.println("No grades entered yet.");
            return;
        }

        double sum = 0, max = allGrades.get(0), min = allGrades.get(0);
        for (double g : allGrades) {
            sum += g;
            if (g > max) max = g;
            if (g < min) min = g;
        }
        double avg = sum / allGrades.size();

        System.out.println("\n---------- CLASS STATISTICS ----------");
        System.out.println("Total Students   : " + students.size());
        System.out.println("Total Grades     : " + allGrades.size());
        System.out.printf("Class Average    : %.2f%n", avg);
        System.out.printf("Highest Grade    : %.2f%n", max);
        System.out.printf("Lowest Grade     : %.2f%n", min);
    }

    static Student selectStudent() {
        System.out.println("Select a student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).name);
        }
        int idx = readInt("Enter number: ") - 1;
        if (idx < 0 || idx >= students.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return students.get(idx);
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
        return val;
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }
}
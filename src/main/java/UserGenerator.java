public class UserGenerator {
    public static User getDefault() {
        return new User("test-datatestdbnk@yandex.ru", "123453", "Ivankt");
    }

    public static User getWithoutEmail() {
        return new User("", "", "Ivane");
    }

    public static User getWithoutPassword() {
        return new User("test-dataaalyrryotd@yandex.ru", "", "Ivane");
    }

    public static User getWithoutName() {
        return new User("test-dataaalyrryotd@yandex.ru", "12345", "");
    }

    public static User getNonexistentUser() {
        return new User("test-dataaalyrryotx@yandex.ru", "12347", "");
    }

    public static User getUserChangeData() {
        return new User("test-datanewlyrryotd@yandex.ru", "123476", "Alex");
    }
}
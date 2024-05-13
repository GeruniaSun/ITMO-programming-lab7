import lt.shgg.data.User;

import java.util.Scanner;

public class Authorisator {
    public static User authorise(){
        var in = new Scanner(System.in);
        System.out.println("Вы уже смешарик?\n" +
                "Введите YES, если у вас уже есть аккаунт, и что-угодно в противном случае: ");
        if (in.nextLine().equalsIgnoreCase("YES")) return enter();
        else return registration();
    }

    private static User registration(){
        var in = new Scanner(System.in);
        var novichok = new User();
        while (true) {
            System.out.println("Хорошо, давайте создадим аккаунт, это совсем не больно\n" +
                    "Придумайте и введите себе крутой логин");
            var login = in.nextLine();
            try {
                novichok.setLogin(login);
                //ПРОВЕРКА НА УНИКАЛЬНОСТЬ ЛОГИНА!!!!!!!!!!!!
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        while (true) {
            System.out.println("Класс! Теперь придумайте и введите хитрый пароль");
            var password = in.nextLine();
            try {
                novichok.setPassword(password);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        return novichok;
    }

    private static User enter(){
        var in = new Scanner(System.in);
        System.out.println("Отлично! вы уже смешарик!\n" +
                "Введите логин");
        var login = in.nextLine();
        //ПРОВЕРКА СУЩЕСТВОВАНИЯ ТАКОГО ЮЗЕРА
        // var user = тот чей логин ввели
        var user = new User("PENIS", "1234");//временная заплатка
        while (true){
            System.out.println("Такого знаем. Но вы ли это?\n" +
                    "Введите пароль");
            var password = in.nextLine();
            if (user.getPassword().equals(password)) {//сравнивать по кэшу?
                System.out.println("Угадали!");
                return user;
            } else System.out.println("Не угадали");
        }
    }
}

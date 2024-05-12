import lt.shgg.data.*;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс предоставляющий методы для чтения билетов введенных пользователем и построения объектов из них
 */
public class TicketAppender {
    /**
     * Метод для считывания объекта с консоли построчно, обрабатывает ввод и делает билет
     * @return введенный пользователем билет
     * @throws IllegalStateException если билет не получилось сделать
     */
    public static Ticket appendTicket(){
        var builder = new TicketBuilder();
        var in = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Введите название");
                String input = in.nextLine();
                builder.withName(input);
                break;
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
        }

        while (true) {
            try {
                System.out.println("Введите координаты через пробел");
                String[] input = in.nextLine().split(" ");
                float x = Float.parseFloat(input[0]);
                int y = Integer.parseInt(input[1]);
                try{
                    if (input.length > 2) throw new IllegalArgumentException("Введено слишком много аргументов");
                    builder.withCoordinates(new Coordinates(x, y));
                    break;
                } catch (NullPointerException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
            catch (Exception e){
                System.out.println("Это не подойдет для значения координат");
            }
        }

        while (true) {
            try {
                System.out.println("Введите цену");
                String input = in.nextLine();
                var price = Long.parseLong(input);
                try {
                    builder.withPrice(price);
                    break;
                } catch (NullPointerException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                System.out.println("Это не подойдет для значения цены");
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
        }

        while (true) {
            try {
                System.out.println("Введите один из нижеприведенных типов билета");
                System.out.println(Arrays.toString(Ticket.TicketType.values()));
                String input = in.nextLine().toUpperCase();
                var type = Ticket.TicketType.valueOf(input);
                builder.withType(type);
                break;
            }
            catch (IllegalArgumentException e){
                System.out.println("Такого варианта нет");
            } catch (NoSuchElementException e) {
                System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
        }

        var venueFlag = false;
        while (true) {
            System.out.println("Вы хотите добавить место проведения?");
            System.out.println("Введите YES/NO");
            var answer = in.nextLine();
            if (answer.equalsIgnoreCase("NO")) break;
            else if (answer.equalsIgnoreCase("YES")) {
                venueFlag = true;
                break;
            } else {
                System.out.println("Пожалуйста, введите 'YES' или 'NO'");
            }
        }

        if (venueFlag) {
            var venueBuilder = new VenueBuilder();

            while (true) {
                try {
                    System.out.println("Введите название места проведения");
                    String input = in.nextLine();
                    venueBuilder.withName(input);
                    break;
                } catch (NoSuchElementException e) {
                    System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                    System.exit(999);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("Введите вместимость места проведения");
                    String input = in.nextLine();
                    var capacity = Integer.parseInt(input);
                    try {
                        venueBuilder.withCapacity(capacity);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                    System.exit(999);
                } catch (Exception e) {
                    System.out.println("это не подойдет для вместимости");
                }
            }

            var addressFlag = false;
            while (true) {
                System.out.println("Вы хотите добавить адрес места проведения?");
                System.out.println("Введите YES/NO");
                var answer = in.nextLine();
                if (answer.equalsIgnoreCase("NO")) break;
                else if (answer.equalsIgnoreCase("YES")) {
                    addressFlag = true;
                    break;
                } else System.out.println("Пожалуйста, введите 'YES' или 'NO'");
            }

            if (addressFlag){
                while (true) {
                    try {
                        System.out.println("Введите адрес места проведения");
                        String input = in.nextLine();
                        venueBuilder.withAddress(new Venue.Address(input));
                        break;
                    } catch (NoSuchElementException e) {
                        System.out.println("вы какие-то гадости делаете. Закрываю приложение");
                        System.exit(999);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            builder.withVenue(venueBuilder.build());
        }

        if (builder.isReadyToBuild()){
            return builder.build();
        } else throw new IllegalStateException("Что-то пошло не так во время описания билета");
    }

    /**
     * Аналогичный метод читает объект из файла, в файле объект должен быть в фигурных скобках с полями через запятую
     * @param args строка из файла описывающая билет
     * @return введенный пользователем билет
     * @throws IllegalStateException если билет не получилось сделать
     * @throws IllegalArgumentException если в файле некорректные значения полей билета
     */
    public static Ticket appendTicket(String args){
        try {
            var builder = new TicketBuilder();
            ArrayList<String> values = new ArrayList<>(List.of(args.substring(1)
                    .replace("}", "").split(", ")));
            values.addAll(List.of("", "", "", ""));

            builder.withName(values.get(0).substring(1).replace("'", ""));
            builder.withCoordinates(new Coordinates(Float.parseFloat(values.get(1)), Integer.parseInt(values.get(2))));
            builder.withPrice(Long.parseLong(values.get(3)));
            builder.withCreationDate(LocalDate.parse(values.get(4)));

            builder.withType(Ticket.TicketType.valueOf(values.get(5)));
            if (!values.get(6).contains("null")) {
                var venueBuilder = new VenueBuilder();
                venueBuilder.withName(values.get(6).substring(1).replace("'", ""));
                venueBuilder.withCapacity(Integer.parseInt(values.get(7)));
                if (!values.get(8).contains("null"))
                    venueBuilder.withAddress(new Venue.Address(values.get(8)
                            .substring(1).replace("'", "")));
                builder.withVenue(venueBuilder.build());
            }
            if (builder.isReadyToBuild()) return builder.build();
            else throw new IllegalArgumentException("что-то не так со значениями полей билета");
        } catch (Exception e){
            throw new IllegalStateException("что-то пошло не так во время описания билета" + e.getMessage());
        }
    }
}

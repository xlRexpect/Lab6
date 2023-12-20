package ex1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp {



    @FunctionalInterface
    interface Filtru<T>{
        public boolean test(T p);
    }
    public static void scriere(List<Angajat> lista) {
        try {
            ObjectMapper mapper=new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            File file=new File("src/main/resources/angajat.json");
            mapper.writeValue(file,lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Angajat> citire() {
        try {
            File file = new File("src/main/resources/angajat.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Angajat> persoane = mapper
                    .readValue(file, new TypeReference<List<Angajat>>() {
                    });
            return persoane;

        }catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static void afisare_filtrata1(List<Angajat> listAng, Filtru<Angajat> f) {
        for(Angajat a:listAng)
            if(f.test(a))
                System.out.println(a);
    }

    /*public Optional<Angajat> findAngajatByName(List<Angajat> listAng, String nameToFind) {
        for (Angajat angajat : listAng) {
            if (angajat.getNume().equals(nameToFind)) {
                return Optional.of(angajat);
            }
        }
        return Optional.empty();
    }*/

    public static void main(String[] args) {
        //List<Angajat> pers=new ArrayList<Angajat>();
        List<Angajat> listaAngajat = citire();
        //System.out.println(listaMobilier);
        Scanner scan = new Scanner(System.in);
        int ui = 0;
        do {
            System.out.println("0=exit");
            System.out.println("1=afisare");
            System.out.println("2=cautare angajat");
            System.out.println("3=adaugare");
            System.out.println("4=angajati cu salariu>2500");
            System.out.println("5=angajati care au functie de conducere (si din 2022.04)");
            System.out.println("6=angajati care nu au functie de conducere");
            System.out.println("7=numele angajatiilor cu litere mari");
            System.out.println("8=salarii mici (<3000)");
            System.out.println("9=primul angajat al firmei");
            System.out.println("10=statistici de salarii");
            System.out.println("11=Ion");
            System.out.println("12=angajati in vara 2022");
            ui = scan.nextInt();
            switch (ui) {
                case 0 -> {
                    System.out.println("iesire din program");
                }
                case 1 -> {
                    //for(Angajat m:listaAngajat) {
                    //   System.out.println(m);
                    //}
                    listaAngajat.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.println("numele angajatului?");
                    Scanner scan2 = new Scanner(System.in);
                    String uiName = scan2.nextLine();
                    for (int i = 0; i < listaAngajat.size(); i++) {
                        if (uiName.equals(listaAngajat.get(i).getNume())) {
                            System.out.println(listaAngajat.get(i));
                        }
                    }
                }
                case 3 -> {
                    Scanner sc_ad = new Scanner(System.in);
                    System.out.println("nume?");
                    String nume = sc_ad.nextLine();
                    System.out.println("post?");
                    String post = sc_ad.nextLine();
                    System.out.println("salariu?");
                    Float salariu = sc_ad.nextFloat();
                    LocalDate dataAng = LocalDate.now();
                    Angajat ang = new Angajat(nume, post, dataAng, salariu);
                    if (listaAngajat == null) {
                        listaAngajat = new ArrayList<>();
                        listaAngajat.add(ang);
                    } else {
                        listaAngajat.add(ang);
                    }
                }
                case 4 -> {
                    afisare_filtrata1(listaAngajat,(a)->a.getSalariu()<2500);
                }
                case 5 -> {
                    List<Angajat> listaConducere=listaAngajat
                            .stream()
                            .filter((a)->(a.getPost().toLowerCase().contains("sef") || a.getPost().toLowerCase().contains("director") ))
                            .filter((a)->(a.getData_ang().getYear()==LocalDate.now().getYear()-1) && a.getData_ang().getMonthValue()==4)
                            .collect(Collectors.toList());
                    listaConducere.forEach(System.out::println);
                }
                case 6 -> {
                    List<Angajat> listFaraConducere=listaAngajat
                            .stream()
                            .filter((a)->!(a.getPost().toLowerCase().contains("sef")) && !(a.getPost().toLowerCase().contains("director")))
                            .sorted(((b, a) -> a.compareTo(b)))
                            .collect(Collectors.toList());
                    listFaraConducere.forEach(System.out::println);
                }
                case 7 -> {
                    listaAngajat
                            .stream()
                            .map((a) -> a.getNume().toUpperCase())
                            .forEach(System.out::println);
                }
                case 8 -> {
                    listaAngajat
                            .stream()
                            .filter((a) -> a.getSalariu() < 3000)
                            .map((a) -> a.getSalariu())
                            .forEach(System.out::println);
                }
                case 9 -> {
                    Optional<Angajat> primulAngajat=listaAngajat.stream()
                            //.min((a2, a1) -> Float.compare(a2.getSalariu(), a1.getSalariu()));
                            //.min(Comparator.comparing(Angajat::getData_ang()));
                                    .min((a1,a2)->(a1.getData_ang().compareTo(a2.getData_ang())));
                    primulAngajat.ifPresent(angajat -> {
                        System.out.println("Primul angajat: " + angajat.getNume() + ", Salariu: " + angajat.getSalariu());
                    });

                    if (primulAngajat.isEmpty()) {
                        System.out.println("Nu există angajați în firmă.");
                    }

                }
                case 10 -> {
                    DoubleSummaryStatistics statistics = listaAngajat.stream()
                            .collect(Collectors.summarizingDouble(Angajat::getSalariu));

                    // Afisam statistici
                    System.out.println("Salariul mediu: " + statistics.getAverage());
                    System.out.println("Salariul minim: " + statistics.getMin());
                    System.out.println("Salariul maxim: " + statistics.getMax());
                }
                case 11 -> {
                    Optional<Angajat> angajatIonOptional = listaAngajat.stream()
                            .filter(angajat -> angajat.getNume().equals("Ion"))
                            .findAny();

                    angajatIonOptional.ifPresentOrElse(
                            angajat -> System.out.println("Firma are cel puțin un Ion angajat"),
                            () -> System.out.println("Firma nu are nici un Ion angajat")
                    );
                }
                case 12 -> {
                    List<Angajat> listaAngajatVaraAnTrecut = listaAngajat
                            .stream()
                            .filter(a -> {
                                LocalDate today = LocalDate.now();
                                LocalDate previousYearMay31 = LocalDate.of(today.getYear() - 1, 5, 31);
                                LocalDate previousYearAug31 = LocalDate.of(today.getYear() - 1, 8, 31);
                                return ((a.getData_ang().isAfter(previousYearMay31)) && (a.getData_ang().isBefore(previousYearAug31)));
                            })
                            .collect(Collectors.toList());

                    listaAngajatVaraAnTrecut.forEach(System.out::println);
                }
                case 13->{
                   // Optional<Angajat> angajatWithName=findAngajatByName("Boros Titusz");
                }
            }
        }
        while (ui != 0);
        scriere(listaAngajat);
    }
}
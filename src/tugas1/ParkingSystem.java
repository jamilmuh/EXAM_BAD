package tugas1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Kendaraan {
    protected String tipe;

    protected String platNomor;
    protected int durasi;
    protected int biaya;

    public Kendaraan(String platNomor, int durasi) {
        this.platNomor = platNomor;
        this.durasi = durasi;
    }

    public abstract void hitungBiaya();
}

class Motor extends Kendaraan {
    public Motor(String platNomor, int durasi) {
        super(platNomor, durasi);
    }

    @Override
    public void hitungBiaya() {
        int jamPertama = 1000;
        int jamBerikutnya = 2000;
        int maxBiaya = 20000;

        tipe = "tugas1.Motor";

        if (durasi <= 1) {
            biaya = jamPertama;
        } else {
            biaya = jamPertama + (durasi - 1) * jamBerikutnya;
        }

        biaya = Math.min(biaya, maxBiaya);
    }
}

class Mobil extends Kendaraan {
    public Mobil(String platNomor, int durasi) {
        super(platNomor, durasi);
    }

    @Override
    public void hitungBiaya() {
        int jamPertama = 3000;
        int jamBerikutnya = 5000;
        int maxBiaya = 68000;

        tipe = "tugas1.Mobil";

        if (durasi <= 2) {
            biaya = jamPertama;
        } else {
            biaya = jamPertama + (durasi - 2) * jamBerikutnya;
        }

        biaya = Math.min(biaya, maxBiaya);
    }
}

public class ParkingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Kendaraan> parkir = new ArrayList<>();

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Masukkan data parkir");
            System.out.println("2. Lihat data parkir");
            System.out.println("3. Keluar");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Clear newline character

            switch (choice) {
                case 1:
                    System.out.print("Plat Nomor: ");
                    String platNomor = scanner.nextLine();
                    System.out.print("Durasi Parkir (jam): ");
                    int durasi = scanner.nextInt();
                    scanner.nextLine();  // Clear newline character

                    System.out.print("Tipe tugas1.Kendaraan (motor/mobil): ");
                    String tipe = scanner.nextLine();

                    Kendaraan kendaraan;
                    if (tipe.equalsIgnoreCase("motor")) {
                        kendaraan = new Motor(platNomor, durasi);
                    } else if (tipe.equalsIgnoreCase("mobil")) {
                        kendaraan = new Mobil(platNomor, durasi);
                    } else {
                        System.out.println("Tipe kendaraan tidak valid.");
                        continue;
                    }

                    kendaraan.hitungBiaya();
                    parkir.add(kendaraan);
                    System.out.println("Data parkir berhasil ditambahkan.");
                    break;

                case 2:
                    System.out.println("Data Parkir:");
                    if (parkir.isEmpty()) {
                        System.out.println("Tidak ada data parkir");
                    }
                    for (int i = 0; i < parkir.size(); i++) {
                        var parkirData = parkir.get(i);
                        System.out.println((i + 1) + ". " + parkirData.tipe + " - " + parkirData.platNomor + " - " + parkirData.durasi + " jam" + " - " + "Rp " + parkirData.biaya);
                    }
                    break;

                case 3:
                    System.out.println("Terima kasih!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }
}

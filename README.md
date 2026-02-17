# Queens Linkedin Solver

## Deskripsi Program
Program ini merupakan solver untuk permainan Queens Linkedin yang menggunakan algoritma Brute Force. Program dapat menyelesaikan puzzle dengan mencari susunan ratu pada papan yang memenuhi constraint tertentu. Program menyediakan antarmuka GUI berbasis JavaFX untuk kemudahan penggunaan dan visualisasi solusi.

![queens-solver-simulation](https://github.com/user-attachments/assets/a45be6d8-5f2d-4c6b-a613-56bea3c8a861)

## Requirement Program
- Java Development Kit (JDK) 11 atau lebih tinggi
- Apache Maven 3.6 atau lebih tinggi
- JavaFX SDK (sudah termasuk dalam dependency Maven)

### Instalasi
1. Install JDK 11+ dari [Oracle](https://www.oracle.com/java/technologies/downloads/) atau [OpenJDK](https://openjdk.org/)
2. Install Maven dari [Apache Maven](https://maven.apache.org/download.cgi)
3. Pastikan Java dan Maven sudah terdaftar di PATH sistem

## Cara Mengkompilasi Program
Untuk mengkompilasi program, jalankan perintah berikut di terminal:
```bash
mvn clean compile
```

Untuk membuat file JAR executable:
```bash
mvn clean package
```

## Cara Menjalankan Program

### Opsi 1: Menjalankan GUI dengan JAR
Setelah kompilasi, jalankan file JAR yang telah dibuat:
```bash
java -jar target/queens-linkedin-solver.jar
```

atau _double click_ file executable `queens-solver.jar`

### Opsi 2: Menjalankan GUI dengan Maven
Jalankan langsung menggunakan Maven plugin:
```bash
mvn clean javafx:run
```

## Cara Menggunakan Program
1. Jalankan program menggunakan salah satu cara di atas
2. GUI akan muncul dengan papan permainan
3. Input konfigurasi puzzle sesuai kebutuhan
4. Klik tombol "Solve" untuk mencari solusi
5. Program akan menampilkan solusi yang ditemukan menggunakan algoritma Brute Force

## Author
**Emilio Justin - 13524043**
